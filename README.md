# Chicago Taxis

CLI application developed for the **ISIS1206 Data Structures** course to analyze Chicago taxi trip data with custom data structures (hash tables, red-black trees, priority queues, and weighted directed graphs).

![Java](https://img.shields.io/badge/Java-Project-007396?logo=java&logoColor=white)
![Build](https://img.shields.io/badge/build-javac-informational)
![License](https://img.shields.io/badge/license-MIT-green)

## Project: Taxi Service in the City of Chicago

This project seeks to assist Chicago city administrators and authorities in their decision-making. To analyze this service, we use data from: https://data.cityofchicago.org/Transportation/Taxi-Trips/wrvz-psew

The source code allows selecting one of 3 subsets (`small`, `medium`, `large`) and then running three functional requirements from an interactive menu:

1. Report top companies by affiliated taxis and services.
2. Identify top taxis by daily alpha points (single date or date range).
3. Find the best schedule to travel between two Community Areas.

## Tech Stack

- Java source code under `src/`
- External libraries in `lib/`:
  - `opencsv-5.2.jar`
  - `commons-lang3-3.0.1.jar`
- Manual compilation and execution with `javac` / `java`

## Installation

1. Ensure Java is installed.
2. Place the dataset CSV files in a `data/` folder at repository root with names like:
   - `taxi-trips-wrvz-psew-subset-small.csv`
   - `taxi-trips-wrvz-psew-subset-medium.csv`
   - `taxi-trips-wrvz-psew-subset-large.csv`

## Usage

Compile:

```bash
javac -cp "lib/*" -encoding UTF-8 -d bin $(find src -name "*.java")
```

Run:

```bash
java -cp "bin:lib/*" main.Main
```

When the program starts, enter dataset size (`small`, `medium`, or `large`) and then choose a functional requirement from the menu.

## Project Structure

```text
src/
  main/Main.java                    # Entry point
  controller/Controller.java        # Program flow and menu handling
  view/View.java                    # Console output
  model/
    logic/                          # Domain model and core logic
    data_structures/                # Custom hash table, RBT, graph, PQ, etc.
    comparators/                    # Comparators + partial heapsort utilities
lib/                                # Third-party jars (OpenCSV, Commons Lang)
```

## Notes on Testing

This repository does not include an automated test suite; validation is performed by compiling and running the CLI application.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).

## Problem Representation

We create classes to represent each Company and Taxi in the dataset. Companies have a name, a number of taxis, and a number of services provided. Taxis have an identifier; in addition, for each date, they have a total number of miles traveled, money collected, and services provided.

We load the trips into a directed graph with weights whose nodes are Community Areas and whose arcs are the average time (in seconds) it takes to go from one Community Area to the other. For each Time (rounded to the nearest 15 minutes) we have a different graph. Trips are added to the graph corresponding to their start time.

For handling Dates and Times we also implement our own classes. Each one implements the equals(), compareTo() and hashCode() methods so that they can be used in data structures correctly. However, we do not verify that the dates/times are valid beyond their correct format.

Our model implements Hash tables to quickly access companies by name and taxis by ID. Instead, to access dates and times we implement RedBlackTrees because they allow us to quickly access information within a specific range. To find the fastest route, it is natural to represent the problem as a graph.

## Loading information

The spatial complexity is analyzed in more detail in each requirement. The temporal complexity is described below:

### Companies:

- First we check if the company has already been saved. If not, we add it. Time: $O(1)$ by using Hash table; It may take time proportional to the size of the table if a rehash is required, but this time is amortized in the long term.
- Then the company is obtained and a new service is added to it. Time: $O(1)$.

In total it takes $O(N)$, where N is the amount of data, since N constant time operations are repeated.

### Taxis:

- First, it is checked if the taxi has already been saved. If not, it is added and a new taxi is added to the company. Time: $O(1)$, since the possible rehashes are amortized in the long term.
- Then, the taxi is obtained and a new service is added to it. That is: the information of the current date is added to each of the 3 RBT trees, adding the new date if necessary. Time: $O(\log F)$, where F is the number of dates found in the tree.

In total, the average is $O(N)$ since in most applications F can be neglected, as will be detailed later.

### Graphs:

- First, the graph for the current timetable is checked if it already exists and it is added if necessary. Time: $O(1)$.
- Then, the graph for the current timetable is obtained. Time: $O(\log H)$, where H is the number of timetables found in the tree.
- Then, the new arc is added or updated to the graph. That is:

1. If the vertices do not exist, they are added.
2. The current arc between the pair of vertices is obtained and if it exists, it is updated; if not, it is added.

Time: $O(degree(V))$ which is what it takes to search for an arc in the adjacency list, since adding vertices and updating/adding arcs takes constant time.

In total, it takes time proportional to $O(NE^2)$, where E is the number of arcs, because each time a new arc is added, it must be checked at its origin vertex to see if it is already present. $\log H$ is ignored since in most applications H can be neglected, as will be detailed later.

## Requirements

### Part A:

To retrieve the total number of taxis and the total number of companies, it is only necessary to ask for the size of the respective Hash table. Therefore, its time complexity is $O(1)$.

To obtain the top M companies according to the number of affiliated taxis, we use an auxiliary class that implements partial Heapsort: it builds a priority queue, adds the first M elements and from there on it adds a new element and removes the one that should not be in the top. The top M according to services provided is equivalent, so we will consider only one of these to analyze the complexities:

The Heapsort class goes through N data and for each one it performs insertion and deletion operations in a PriorityQueue of M+1 elements (approximated to M from now on). Each insertion/deletion operation has a time cost of $O(\log M)$. Therefore, the time complexity is $O(N\log M)$. In typical applications $M<<<N$, so the complexity would be $O(N)$; in contrast, if the N data are to be sorted, $M=N$ and the complexity is $O(N\log N)$. Finally, the spatial complexity is the additional size of the PriorityQueue, i.e., $O(M)$. If the value of M is small, it can be neglected and constant spatial complexity can be assumed.

Since we store the taxis and companies in Hash tables, we need space proportional to the number of taxis and companies in the dataset. For example, in the _large_ dataset there are 4139 taxis and 50 companies. However, since we use LinearProbing which doubles in size when it reaches 50% capacity, we have an approximate spatial complexity of $O(2T+2C)$, where T is the number of taxis and C the number of companies in the dataset. Here we ignore that each Taxi has additional information per date since the dataset corresponds to a single year; In this way, the additional size is limited and by ignoring it we can simplify the mathematical analysis.

### Part B:

To build the top, the same partial Heapsort scheme explained for the previous requirement is followed. In this sense, there is a spatial complexity of $O(M)$ to build the PriorityQueue and $O(2T)$ to store the Taxi information, where M is the number of Taxis to be included in the top and T the number of Taxis in the dataset. For typical applications, this additional size can be ignored since it is much smaller than N, the number of trips to be analyzed.

On the other hand, the temporal complexity is different since the Taxi comparator does not respond in constant time as the Company comparator does. Each Taxi returns the value of its daily alpha function by consulting the total miles, the money received and the number of services provided on the date or between the specified dates, as the case may be. Let F be the number of dates that the taxi stores. If the date is a single date, the Taxi responds in time proportional to $O(\log F)$; whereas if the date range is given, in the worst case all the elements of the tree have to be traversed, i.e. the Taxi responds in time proportional to $O(F)$.

Again, the Heapsort class processes N data with a PriorityQueue of M elements. Each operation on the PriorityQueue performs on average $O(\log M)$ comparisons, i.e. it takes time proportional to $O((\log F)(\log M))$ for the single date case and $O(F\log M)$ for the date range. The time complexity of the algorithm is $O(N(\log F)(\log M))$ for single dates and $O(NF\log M)$ for date ranges. In typical applications, the number of dates and the value of M can be neglected, as done above. Therefore, the time complexity is approximately $O(N)$, but we can expect the algorithm for a range of dates to take longer than the algorithm for a single date.

### Part C:

First, the graphs for each timetable within the specified range are obtained, which are stored in a RedBlackTree of H elements, where H is the number of timetables in the dataset. In this application, timetables of approximately 15 minutes were saved, meaning that for each hour of the day there are 4 possible timetables; thus, there are a total of 96 possible timetables. Although recovering the graphs in a range of timetables uses time proportional to $O(H)$, this value is small enough to be ignored.

Second, Dijkstra's algorithm is applied to each graph and the result that generates the shortest time is saved. Dijkstra takes time proportional to $O(E\log V)$, where E is the number of arcs and V is the number of nodes; this is the time complexity of the algorithm, because as stated before, it can be ignored that this operation is performed H times since H is bounded by a small number. Additionally, since the nodes are Community Areas, the critical factor determining the complexity of this algorithm is the number of arcs, which in certain applications can be proportional to the amount of data processed N.

With respect to spatial complexity, there are H graphs, each represented by adjacency lists, so each graph occupies a space proportional to $O(E+V)$. On the other hand, when applying Dijkstra, an indexed PriorityQueue, a distTo array and an edgeTo array are required; each takes up space proportional to $O(V)$. Therefore, the spatial complexity is $O(H(E+4V))$, which if the value of H is ignored, is proportional to the number of arcs and nodes in the graphs.

## Considerations

When loading the data, all records that met any of the following conditions were ignored:

- null `trip_seconds`, `trip_miles`, `trip_total`, `pickup_community_area` or `dropoff_community_area`.
- `trip_miles` or `trip_total` less than or equal to 0.
- `pickup_community_area` equal to `dropoff_community_area`.

Also, to simplify, each taxi was added only to the first company with which it appears affiliated in the dataset, so that there were companies with 0 affiliated taxis but with reported services. Those companies that did not have affiliated taxis at the end of the entire data loading were eliminated from the application, since we consider that there are few cases.

Taxis that do not report a company are saved as "Independent Owner"; however, when testing our application we did not find any such cases.

The analysis did not take into account that the number of companies and taxis can grow proportionally to the amount of data in certain applications. It was also assumed that the number of dates is a sufficiently low number so as not to significantly impact the application's performance.
