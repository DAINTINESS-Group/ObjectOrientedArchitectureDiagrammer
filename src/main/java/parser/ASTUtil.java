package parser;

import com.github.javaparser.ast.Modifier;

import java.util.List;

public class ASTUtil {

    public static List<Modifier> filterVisibilityModifiers(List<Modifier> allModifiers){
        return allModifiers
                .stream()
                .filter(modifier -> {
                    Modifier.Keyword keyword = modifier.getKeyword();
                    return keyword.equals(Modifier.Keyword.PUBLIC) ||
                            keyword.equals(Modifier.Keyword.PRIVATE) ||
                            keyword.equals(Modifier.Keyword.PROTECTED);
                }).toList();
    }
}
