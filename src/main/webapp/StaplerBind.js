function isArrayIndexDefined(arrayObject, index) {
    if(arrayObject[index] !== null && arrayObject[index] !== undefined) {
        return true;
    } else {
        return false;
    }
}
