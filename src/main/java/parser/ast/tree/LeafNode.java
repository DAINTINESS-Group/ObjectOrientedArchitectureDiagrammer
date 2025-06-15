package parser.ast.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record LeafNode(
        Path path,
        String nodeName,
        NodeType nodeType,
        String baseClass,
        PackageNode parentNode,
        List<String> implementedInterfaces,
        List<Method> methods,
        List<Field> fields,
        Map<String, String> variables,
        List<String> imports,
        List<String> records,
        List<LeafNode> innerClasses,
        List<String> innerEnums,
        List<String> createdObjects) {

    public List<String> getMethodReturnTypes() {
        return methods.stream()
                .map(it -> it.returnType)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getMethodParameterTypes() {
        return methods.stream()
                .flatMap(it -> it.parameters().values().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public record Method(
            String name,
            String returnType,
            ModifierType modifierType,
            Map<String, String> parameters) {}

    public record Field(String name, String fieldType, ModifierType modifierType) {}
}
