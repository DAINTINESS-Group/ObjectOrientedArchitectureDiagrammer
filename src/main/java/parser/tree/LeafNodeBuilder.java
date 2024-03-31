package parser.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafNodeBuilder
{
    private final Map<String, String>   variables;
    private final List<LeafNode.Method> methods;
    private final List<LeafNode.Field>  fields;
    private final List<String>          imports;
    private final List<String>          implementedInterfaces;
    private final List<String>          createdObjects;
    private final List<LeafNode>        innerClasses;
    private final List<String>          innerEnums;
    private final List<String>          records;
    private final PackageNode           parentNode;
    private final Path                  path;
    private       String                nodeName;
    private       String                baseClass;
    private       NodeType              nodeType;


    public LeafNodeBuilder(PackageNode parentNode, Path path)
    {
        this.parentNode       = parentNode;
        this.path             = path;
        implementedInterfaces = new ArrayList<>();
        createdObjects 	      = new ArrayList<>();
        innerClasses 		  = new ArrayList<>();
        innerEnums 		      = new ArrayList<>();
        records 		   	  = new ArrayList<>();
        variables 			  = new HashMap<>();
        imports 		   	  = new ArrayList<>();
        methods 			  = new ArrayList<>();
        fields 			      = new ArrayList<>();
        nodeType 			  = null;
        baseClass 			  = "";
        nodeName 			  = "";
    }


    public LeafNodeBuilder setVariables(Map<String, String> variables)
    {
        this.variables.putAll(variables);
        return this;
    }


    public LeafNodeBuilder setInnerClasses(List<LeafNode> innerClasses)
    {
        this.innerClasses.addAll(innerClasses);
        return this;
    }


    public LeafNodeBuilder setNodeType(NodeType nodeType)
    {
        this.nodeType = nodeType;
        return this;
    }


    public LeafNodeBuilder setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
        return this;
    }


    public LeafNodeBuilder setBaseClass(String baseClass)
    {
        this.baseClass = baseClass;
        return this;
    }


    public LeafNodeBuilder setImplementedInterface(List<String> interfaces)
    {
        this.implementedInterfaces.addAll(interfaces);
        return this;
    }


    public LeafNodeBuilder setCreatedObjects(List<String> createdObjects)
    {
        this.createdObjects.addAll(createdObjects);
        return this;
    }


    public LeafNodeBuilder setInnerEnums(List<String> enums)
    {
        this.innerEnums.addAll(enums);
        return this;
    }


    public LeafNodeBuilder setRecords(List<String> records)
    {
        this.records.addAll(records);
        return this;
    }


    public LeafNodeBuilder setImports(List<String> imprts)
    {
        this.imports.addAll(imprts);
        return this;
    }


    public LeafNodeBuilder setMethods(List<LeafNode.Method> methods)
    {
        this.methods.addAll(methods);
        return this;
    }


    public LeafNodeBuilder setFields(List<LeafNode.Field> fields)
    {
        this.fields.addAll(fields);
        return this;
    }


    public LeafNode build()
    {
        return new LeafNode(path,
                            nodeName,
                            nodeType,
                            baseClass,
                            parentNode,
                            implementedInterfaces,
                            methods,
                            fields,
                            variables,
                            imports,
                            records,
                            innerClasses,
                            innerEnums,
                            createdObjects);
    }

}
