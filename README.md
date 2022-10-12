<div align="center">
  <a href=https://github.com/dimanyfantakis/ObjectOrientedArchitectureDiagrammer>
    <img src="src/main/resources/assets/logo.png" alt="Logo" width="250" height="250">
  </a>
  <h1>Object Oriented Architecture Diagrammer</h1>
  <p>
    A tool for the reverse engineering of Java object-oriented source code into Unified Modeling Language (UML) diagrams
  </p>
  
[![Contributors][contributors-shield]][contributors-url]
[![Commits][commits-shield]][commits-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

[**View Demo**][demo-url] · [**User Documentation**][userDocumentation-url] · [**Report Bug**][issues-url] · [**Request Feature**][issues-url]

</div>

# Table of Contents
> - [About](#about)
>   * [Motivation](#motivation)
>   * [Features](#features)
> - [Getting Started](#getting-started)
>   * [Requirements](#requirements)
>   * [Installation](#installation)
>   * [Usage](#usage)
>* [Contributing / Reporting issues](#contributing--reporting-issues)
>* [Roadmap](#roadmap)
>* [Authors](#authors)
>* [License](#license)
>* [Acknowledgments](#acknowledgments)

## About

### Motivation

The purpose of this project is to develop an independent software tool that creates UML diagrams by reverse engineering Java object-oriented source code. 

### Features

* Visualize class && package UML diagrams 
  * Choose the classes/packages that will be included in the diagram
  * Create different diagrams from the same project
* Exported a created diagram:
  * GraphML. View the exported diagram using yEd
  * as Image
  * as a text file that can you can load later

## Getting Started

### Requirements

1. Clone the repository
  ```bash
  git clone https://github.com/dimanyfantakis/ObjectOrientedArchitectureDiagrammer.git
  ```

2. Import project into:
  * [Eclipse][importEclipse-url]
  * [Intellij IDEA][importIntellij-url]

### Installation

1. Run
  ```bash
  mvn compile
  ```
to build the project.

2. 
* In Eclipse: Project -> Build
* In Intellij IDEA : Build -> Project

### Usage

1. Run
  ```bash
  mvn package
  ```
to create the jar **ObjectOrientedArchitectureDiagrammer-0.0.1-SNAPSHOT-jar-with-dependencies.jar**

2. Run
  ```bash
  java -jar ObjectOrientedArchitectureDiagrammer\target\ObjectOrientedArchitectureDiagrammer-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  ```
to execute the jar

Check the [User Documentation][userDocumentation-url] regarding more about the usage of the tool

### Tests

To run the tests simply run
  ```bash
  mvn test
  ```

## Contributing / Reporting issues

### Code of Conduct

Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. Don't forget to give the project a star!

1. Fork the Project
2. Create your Feature Branch
  ```bash
  git checkout -b feature/NewFeature
  ```
3. Commit your Changes
  ```bash
  git commit -m 'Add some NewFeature'
  ```
4. Push to the Branch
  ```bash
  git push origin feature/NewFeature
  ```
5. Open a Pull Request

### Issues

Create a new [Issue][issues-url] to report any issues

## Roadmap

- [ ] Change the parser to support
  - [ ] Local fields
  - [ ] Enums
- [ ] Build a visualization library
    - [ ] Support of drag & drop canvas to add UML entities
    - [ ] Movable & deletable nodes
    - [ ] Implement a layout algorithm that uses bend minimization
- [ ] Support [PlantUML][plantuml-url]
  - [ ] Use PlantUML’s language to define the diagram
  - [ ] Export image of diagram
  - [ ] Visualize the diagram within the tool's canvas
    
## Authors
Dimitris Anyfantakis

## License

See [License][license-url] for more information regarding the license

## Acknowledgments

[contributors-shield]: https://img.shields.io/github/contributors/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[contributors-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/graphs/contributors
[commits-shield]: https://img.shields.io/github/last-commit/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[commits-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/commit/main
[forks-shield]: https://img.shields.io/github/forks/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[forks-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/network/members
[stars-shield]: https://img.shields.io/github/stars/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[stars-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/stargazers
[issues-shield]: https://img.shields.io/github/issues/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[issues-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/issues/
[license-shield]: https://img.shields.io/github/license/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[license-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer/blob/main/LICENSE
[userDocumentation-url]: https://docs.google.com/document/d/1C8zHygW2cqtbSBOQGcSFhKYcRx0AkQaY/edit?usp=sharing&ouid=111456297792845674932&rtpof=true&sd=true
[demo-url]: https://github.com/DAINTINESS-Group/ObjectOrientedArchitectureDiagrammer
[importEclipse-url]: https://www.baeldung.com/maven-import-eclipse
[importIntellij-url]: https://www.jetbrains.com/idea/guide/tutorials/working-with-maven/importing-a-project/
[plantuml-url]: https://plantuml.com/