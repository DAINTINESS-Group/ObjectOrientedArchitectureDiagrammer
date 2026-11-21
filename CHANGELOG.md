# Changelog

## v0.3 (2026) - Aristeidis Dragatis, Diploma Thesis, Univ. Ioannina

Fixes and extensions of the AST parser, its outputs and its diagnostics. Every change was made test-first: a failing test was committed first, and then the change that makes it pass. All changes are merged into `dev`.

### Parser

- **Arcs in an isolated package.** The classes of a package without dependencies to other packages got no arcs at all, not even `extends` or `implements`. `ASTInterpreter.addVertexArcs` skipped such a package before it added the arcs of its classes. ([#50](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/50))
- **Files that are not valid Java.** One invalid file stopped the parsing of the whole project, so no diagram was created. Such a file is now skipped with a warning, and the rest of the project is parsed. ([#52](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/52))
- **Inner classes.** An inner class now has its own vertex, named `Outer::Inner`, with its own `extends`, `implements`, fields, methods and constructors. Before, all of them were counted to the outer class, which could then appear to extend two classes. A new `NESTING` relationship connects an outer class to its inner classes, and choosing a class in the project tree also chooses its inner classes. ([#53](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/53))
- **Records.** A project with a `record` in its own file crashed with a `NullPointerException`. A record is now parsed as a class, with the interfaces it implements, and its components become private fields, as the Java compiler makes them. ([#55](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/55))
- **Package declarations.** Each class now knows the package that its file declares (`LeafNode.packageName`) and its full name, `package.Class` (`LeafNode.fullName`). `ASTParser.createClassPackages` maps the full name of every class to its package. The key is the full name, because the same class name can exist in two packages. ([#58](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/58))

### Outputs

- **Nesting.** The `NESTING` relationship is drawn as a line with a circled plus (⊕) at the outer class, in PlantUML ([#53](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/53)) and in the GUI ([#54](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/54)). In GraphML it has an empty circle, because yEd has no ⊕. `ShadowCleaner` keeps the nesting line when the two classes also have another relationship. ([#54](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/54))

### Logging

- **The log4j configuration.** The tool loaded the `log4j2.xml` of the ProGuard jar instead of its own, so no `info` message was shown, and warnings had no time or level. The configuration of the project is now at the root of the resources, so it is found first. ([#56](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/56))
- **The AST log.** When a class diagram is created, the log shows the relationships of its classes, one per line, in a form that can be searched with `grep`. A line is `FOUND` when the parser created the arc, `EXTERNAL` when the parent is outside the project, `MISSED` when the parent is in the project but there is no arc (a parser bug), and `SKIPPED` for a file that is not valid Java. For example, in `BookstoreAdvancedV01` with `Book`, `Item` and `Gui` chosen:
  ```
  INFO DIAGRAM Book, Gui, Item
  INFO FOUND extension Book -> Item
  INFO EXTERNAL extension Gui -> Application
  ```
  The `main` of `ParserDiagnostics` prints the lines of a whole project. ([#57](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/57))

### Tests

- **Regression tests.** Tests for typical cases in the `BookstoreAdvancedV01`, `LatexEditor` and `ParserTesting` projects, and in a new fixture, `CollectionSamples`, with the assertions API. The API was extended with:
    - negative checks: `doesNotExtendClass`, `doesNotImplementInterface`, `hasNoDependencyTo` (for classes and packages) and `hasNoAssociationTo`;
    - `hasAggregationTo` and `hasNoAggregationTo`;
    - `assertThatClassifier`, which finds a class, an interface or an enum;
    - clearer messages when a class, an interface or a package is not in the model;
    - a first rule over the whole model, `everyClassExtendsAtMostOneClass`.

  Known limits of the parser are kept as `@Disabled` tests, ready for the day they are fixed. ([#51](https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/pull/51))
- Every fix above came with its own tests. 

### Known limitations

- **Parser**
    - An inner class that extends another inner class of the same outer class gets no arc. The AST log reports it as `MISSED`.
    - There is no composition, and aggregation is found only for some collections, and sometimes for types that only look like collections.
- **Packages:** the app still shows the names of the folders, in the project tree and in the package diagram. The map of `createClassPackages` is not used by the app yet.
- **Outputs**
    - yEd has no ⊕, so GraphML shows an empty circle.
    - In the GUI, the diamond of an aggregation is never drawn.