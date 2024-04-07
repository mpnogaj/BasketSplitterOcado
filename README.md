Basket Splitter
===
Marcin_Nogaj_Java_Wrocław 

Java library for efficient splitting items into categories. 
Each item can be placed into finite number of groups, 
defined by config file

## Table of contents
* [General info](#general-info)
* [Usage example](#usage-example)
* [Technologies](#technologies)
* [Building library](#building-library)

## General info

The problem of splitting items into groups can be viewed as a 
variant of the Set Cover Problem, which is known to be NP-hard. 
In essence, the task involves partitioning a set of items into 
the smallest possible number of groups while ensuring that each 
item belongs to at least one group. This optimization challenge 
arises in diverse fields such as logistics, resource allocation, 
and data organization.

Due to its NP-hard nature, there is no polynomial algorithm that 
will find the optimal answer. While there are some greedy 
approximation algorithms available, I decided to use backtracking technique
which will result in exponential complexity, but because the number of sets is
limited to 10, it will work fine.

The main idea behind solving the Set Cover Problem with backtracking
is to systematically explore different combinations of sets to cover
all elements in the universe while minimizing the total number of sets
used.

## Usage example
```java
class Example {
    public void example() {
        final var absolutePathToConfigFile = "/path/to/config/file.json";
        final var items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan",
                "Table Cloth 54x72 White", "Flower - Daisies");

        final var basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        final var result = basketSplitter.split(items);

        for (final var entry : result.entrySet()) {
            final var group = entry.getKey();
            final var items = entry.getValue();
            System.out.println("Delivery method " + key + " contains items: " + value);
        }
    }    
}
```

## Technologies

Project is built with:
* Java 21
* Gradle 8.7
* Gson 2.10.1
* JUnit Jupiter 5.10.2

## Building library

To build library from sources you can use convenient gradle tasks
### To build normal JAR
```bash
./gradlew jar
```
### To build self-contained JAR (fat JAR)
```bash
./gradlew fatJar
```