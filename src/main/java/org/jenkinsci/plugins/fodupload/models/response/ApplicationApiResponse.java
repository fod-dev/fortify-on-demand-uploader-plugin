public class ApplicationApiResponse {
    private int ApplicationId;
    private String ApplicationName;
    private String ApplicationDescription;
    private String ApplicationCreatedDate;
    private int BusinessCriticalityTypeId;
    private String BusinessCriticalityType;
    private String EmailList;
    private int ApplicationTypeId;
    private String ApplicationType;
    private Boolean HasMicroservices;
    Private List<ApplicationAttributeExtended> Attributes;

    public int getApplicationId() {
        return ApplicationId;
    };
    public String getApplicationName() {
        return ApplicationName;
    };
    public String getApplicationDescription() {
        return ApplicationDescription;
    };
    public String getApplicationCreatedDate() {
        return ApplicationCreatedDate;
    };
    public int getBusinessCriticalityTypeId() {
        return BusinessCriticalityTypeId;
    };
    public String getBusinessCriticalityType() {
        return BusinessCriticalityType;
    };
    public String getEmailList() {
        return EmailList;
    };
    public int getApplicationTypeId() {
        return ApplicationTypeId;
    };
    public String getApplicationType() {
        return ApplicationType;
    };
    public Boolean getHasMicroservices() {
        return HasMicroservices;
    };
    public List<ApplicationAttributeExtended> getAttributes() {
        return Attributes;
    };
}

public class ApplicationAttributeExtended {
    private String Name;
    private int Id;
    private String Value;
    
    public String Name() {
        return Name;
    };
    public int Id() {
        return Id;
    };
    public String Value() {
        return Value;
    };
}