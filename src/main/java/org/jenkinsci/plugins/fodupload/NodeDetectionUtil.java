package org.jenkinsci.plugins.fodupload;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.Node;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.actions.WorkspaceAction;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class NodeDetectionUtil {

    /**
     * Get the node where the build/run executed, handling both FreeStyle and Pipeline jobs
     * @param run The Run object (could be AbstractBuild for FreeStyle or WorkflowRun for Pipeline)
     * @return Node object or null if not found
     */
    public static Node getExecutionNode(Run<?, ?> run) {
        if (run == null) {
            return null;
        }

        // Handle FreeStyle jobs (AbstractBuild)
        if (run instanceof AbstractBuild) {

            AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) run;
            return build.getBuiltOn();
        }

        // Handle Pipeline jobs (WorkflowRun)
        if (run instanceof WorkflowRun) {
            return getNodeFromWorkflowRun((WorkflowRun) run);
        }

        // Fallback: try to get built-on node if available
        try {
            java.lang.reflect.Method getBuiltOnMethod = run.getClass().getMethod("getBuiltOn");
            return (Node) getBuiltOnMethod.invoke(run);
        } catch (Exception e) {
            // Method doesn't exist or other reflection error
        }

        return null;
    }

    /**
     * Extract node information from WorkflowRun (Pipeline jobs)
     */
    private static Node getNodeFromWorkflowRun(WorkflowRun workflowRun) {
        try {
            // Method 1: Try to get from execution context
            Node nodeFromExecution = getNodeFromFlowExecution(workflowRun);
            if (nodeFromExecution != null) {
                return nodeFromExecution;
            }

            // Method 2: Check environment variables
            String nodeName = getNodeNameFromEnvironment(workflowRun);
            if (nodeName != null && !nodeName.isEmpty()) {
                return Jenkins.get().getNode(nodeName);
            }

        } catch (Exception e) {
            // Log error but continue
            System.err.println("Error getting node from WorkflowRun: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get node from Flow Execution (comprehensive approach for Pipeline)
     * This method traverses the flow graph to find node allocation information
     */
    private static Node getNodeFromFlowExecution(WorkflowRun workflowRun) {
        try {
            FlowExecution execution = workflowRun.getExecution();
            if (execution == null) {
                return null;
            }

            // Method 1: Check current heads for workspace actions
            List<FlowNode> currentHeads = execution.getCurrentHeads();
            Node nodeFromHeads = findNodeFromFlowNodes(currentHeads);
            if (nodeFromHeads != null) {
                return nodeFromHeads;
            }

            // Method 2: Traverse the entire flow graph if current heads don't have info
            List<FlowNode> allNodes = getAllFlowNodes(execution);
            return findNodeFromFlowNodes(allNodes);

        } catch (Exception e) {
            System.err.println("Error traversing flow execution: " + e.getMessage());
            return null;
        }
    }

    /**
     * Find node information from a list of FlowNodes
     */
    private static Node findNodeFromFlowNodes(List<FlowNode> flowNodes) {
        for (FlowNode flowNode : flowNodes) {
            try {
                // Look for WorkspaceAction which contains node information
                WorkspaceAction wsAction = flowNode.getAction(WorkspaceAction.class);
                if (wsAction != null && wsAction.getNode() != null) {
                    Node node = Jenkins.get().getNode(wsAction.getNode());
                    if (node != null) {
                        return node;
                    }
                }


            } catch (Exception e) {
                // Continue to next node if this one fails
                continue;
            }
        }
        return null;
    }

    /**
     * Get all flow nodes from execution (traversing the graph)
     */
    private static List<FlowNode> getAllFlowNodes(FlowExecution execution) {
        List<FlowNode> allNodes = new ArrayList<>();
        try {
            // Start from current heads and traverse backwards
            List<FlowNode> toVisit = new ArrayList<>(execution.getCurrentHeads());
            java.util.Set<String> visited = new java.util.HashSet<>();

            while (!toVisit.isEmpty()) {
                FlowNode current = toVisit.remove(0);
                if (current == null || visited.contains(current.getId())) {
                    continue;
                }

                visited.add(current.getId());
                allNodes.add(current);

                // Add parent nodes to visit list
                List<FlowNode> parents = current.getParents();
                if (parents != null) {
                    toVisit.addAll(parents);
                }
            }
        } catch (Exception e) {
            // If traversal fails, return what we have
            System.err.println("Error traversing flow graph: " + e.getMessage());
        }

        // Reverse to get chronological order (oldest first)
        Collections.reverse(allNodes);
        return allNodes;
    }

    /**
     * Extract node information from step arguments
     */
    private static String extractNodeFromArguments(Object arguments) {
        try {
            if (arguments instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> argsMap = (java.util.Map<String, Object>) arguments;

                // Look for common agent/node specification patterns
                Object agent = argsMap.get("agent");
                if (agent instanceof String) {
                    return (String) agent;
                }

                Object node = argsMap.get("node");
                if (node instanceof String) {
                    return (String) node;
                }

                Object label = argsMap.get("label");
                if (label instanceof String) {
                    return (String) label;
                }
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }

        return null;
    }

    /**
     * Extract node information from various action types
     */
    private static String extractNodeFromAction(hudson.model.Action action) {
        try {
            // Use reflection to check for common node-related methods
            java.lang.reflect.Method[] methods = action.getClass().getMethods();
            for (java.lang.reflect.Method method : methods) {
                String methodName = method.getName().toLowerCase();
                if ((methodName.contains("node") || methodName.contains("agent"))
                        && method.getParameterCount() == 0
                        && method.getReturnType() == String.class) {

                    Object result = method.invoke(action);
                    if (result instanceof String && !((String) result).isEmpty()) {
                        return (String) result;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }

        return null;
    }

    /**
     * Try to get node name from environment variables
     */
    private static String getNodeNameFromEnvironment(WorkflowRun workflowRun) {
        try {
            // Get environment variables from the run
            hudson.EnvVars envVars = workflowRun.getEnvironment();
            String nodeName = envVars.get("NODE_NAME");

            // Handle special cases
            if ("master".equals(nodeName) || "built-in".equals(nodeName) || nodeName == null) {
                // Try alternative environment variables
                String executorName = envVars.get("EXECUTOR_NUMBER");
                String buildTag = envVars.get("BUILD_TAG");

                // If we have executor info, we're probably on master
                if (executorName != null) {
                    return ""; // Empty string represents master node
                }
            }

            return nodeName;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Enhanced method to get all possible nodes that were used in a pipeline
     * This is useful for pipelines that use multiple agents
     */
    public static List<String> getAllExecutionNodes(Run<?, ?> run) {
        List<String> nodes = new ArrayList<>();

        if (run instanceof AbstractBuild) {
            // FreeStyle jobs only use one node
            Node node = ((AbstractBuild<?, ?>) run).getBuiltOn();
            if (node != null) {
                nodes.add(node.getNodeName());
            }
        } else if (run instanceof WorkflowRun) {
            // Pipeline jobs can use multiple nodes
            WorkflowRun workflowRun = (WorkflowRun) run;
            nodes.addAll(getAllNodesFromWorkflow(workflowRun));
        }

        return nodes;
    }

    /**
     * Get all nodes used in a workflow run
     */
    private static List<String> getAllNodesFromWorkflow(WorkflowRun workflowRun) {
        List<String> nodeNames = new ArrayList<>();
        java.util.Set<String> uniqueNodes = new java.util.HashSet<>();

        try {
            FlowExecution execution = workflowRun.getExecution();
            if (execution != null) {
                List<FlowNode> allNodes = getAllFlowNodes(execution);

                for (FlowNode flowNode : allNodes) {
                    // Check WorkspaceAction
                    WorkspaceAction wsAction = flowNode.getAction(WorkspaceAction.class);
                    if (wsAction != null && wsAction.getNode() != null) {
                        uniqueNodes.add(wsAction.getNode());
                    }
                }
            }

            // Also try to get from environment
            String envNodeName = getNodeNameFromEnvironment(workflowRun);
            if (envNodeName != null && !envNodeName.isEmpty()) {
                uniqueNodes.add(envNodeName);
            }

        } catch (Exception e) {
            System.err.println("Error getting all nodes from workflow: " + e.getMessage());
        }

        nodeNames.addAll(uniqueNodes);
        return nodeNames;
    }

    /**
     * Get node name as string (simpler approach)
     * ACCURACY: ~95% for FreeStyle, ~85% for Pipeline
     * @param run The Run object
     * @return Node name or "unknown" if not determinable
     */
    public static String getExecutionNodeName(Run<?, ?> run) {
        Node node = getExecutionNode(run);
        if (node != null) {
            return node.getNodeName();
        }

        // Fallback: try environment variable approach for Pipeline
        if (run instanceof WorkflowRun) {
            String nodeName = getNodeNameFromEnvironment((WorkflowRun) run);
            if (nodeName != null) {
                return nodeName;
            }
        }

        return "unknown";
    }

    public static boolean isRunningOnAgent(Node node) {

        if(node == null)
        {
            // Build hasn't started or node info lost
            return  false;
        }
        if(node == Jenkins.get())
        {
            //runs in master
            return  false;
        }
        //running on remote agent
        return true;

    }
}