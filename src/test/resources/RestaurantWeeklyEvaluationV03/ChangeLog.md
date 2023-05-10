**September 2022 Changes**

# Changes from v2 to v3 



## Restructuring 
- significant restructuring of the folder structure!
- all eligible visibilities can be set to package-private visibility!! This is due to the fact, that due to the new folder structure, the tests belong to the same package with their classes. So, in order for class XxxTest to test class Xxx, it is no longer needed that class Xxx is public; tests and regular classes are part of the _same_ package now!





# Changes from v1 to v2

## Package dataModel 
- Renaming of Order to OrderItem

## Package dataLoad 

- Renaming of IFullDataLoader.fullDataLoad() to performFullDataLoad()

## Package Reporting 
- introduced package visibility for the actual service classes

## Common fixes everywhere
- Fixing the order of elements inside the file
- Parameters in methods: try to replace all occurrences of ArrayList to List.
- Also: List<...> list = new ArrayList<...>(); inside the code
- Checking the parameters of methods for validity
- trying to avoid methods with a void return type, as much as possible

