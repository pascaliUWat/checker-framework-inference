Continuous integration status of master: 
[![Build Status](https://travis-ci.org/opprop/checker-framework-inference.png?branch=master)](https://travis-ci.org/opprop/checker-framework-inference)


Checker Framework Inference
===========================

This project is a general type inference framework,
built upon the [Checker Framework](https://checkerframework.org/).

Given a program with no type annotations, Checker Framework Inference produces a program with type annotations.

By contrast, given a program with type annotations, the Checker Framework determines verifies the program's correctness or reveals errors in it.


Developer Notes
===============

If you want to extend the framework for your own type system or add
additional constraint solvers, please send us mail.

The checker-framework-inference Google Drive folder contains
additional documents for developers:

https://drive.google.com/drive/u/1/folders/0B7vOZvme6aAOfjQ0bWlFU1VoeVZCVjExVmJLM0lGY3NBV0VLcENYdm03c0RCNGFzZURHX2c

That information is being moved to here in the repository.


Requirements
------------

You will need a JDK (version 8) and gradle.

Following the instructions in the Checker Framework manual to install the Checker Framework from source.

NOTE: gradle on Ubuntu 14.10 hard-codes JAVA_HOME. To change this, edit
    `/usr/share/gradle/bin/gradle`
and replace

````
    export JAVA_HOME=/usr/lib/jvm/default-java
````

with

````
    [ -n "$JAVA_HOME" ] || export JAVA_HOME=/usr/lib/jvm/default-java
````


Building
--------

To build:

````
gradle dist
````


Execution
---------

Verify you have all of the requirements.

````
./scripts/inference
````

is the script used to run inference.

Example:

````
./scripts/inference --logLevel=FINE --mode ROUNDTRIP --checker ostrusted.OsTrustedChecker --solver checkers.inference.solver.PropagationSolver -afud /path/to/Annotation/File/Utilities/output/directory [List of files]
````

There are a couple of required options:

* `--mode`
Specifies what the tools should do.
Available options are [INFER, TYPECHECK, ROUNDTRIP, ROUNDTRIP_TYPECHECK]

  * `INFER`:
    Generates and solves the constraints and writes the results to default.jaif file

  * `TYPECHECK`:
    Typechecks the existin code

  * `ROUNDTRIP`:
    Generates and solves the constraints and then inserts the results
    back into the original source code

  * `ROUNDTRIP_TYPECHECK`:
    Executes roundtrip and then typechecks the result

* `--checker`
Specifies which checker to run.
The three most supported checkers at the moment are
`ostrusted.OsTrustedChecker`,
`checkers.tainting.TaintingChecker` and 
`dataflow.DataflowChecker`.

  You can find details of `dataflow.DataflowChecker` in [README.dataflow](src/dataflow/README.md) 

* `--solver`
Which solver to use on the constraints.

* `--targetclasspath`
The classpath that is required by target program.

`checkers.inference.solver.PropagationSolver` and `checkers.inference.solver.GeneralSolver` are real solvers
at the moment.

Omiting the solver will create an output that numbers all of the
annotation positions in the program.

`checkers.inference.solver.DebugSolver` will output all of the
constraints generated.


Other options can be found by `./scripts/inference --help`.

## Use of General solver

Generic solver is designed for solving type constraints from arbitrary type system.

You can invoke generic solver through:

````
--solver checkers.inference.solver.GeneralSolver
````

There are a couple of arguments that generic solver can accept:

* `solver`
Specifies what concrete solver is going to use. 

  At this moment, we have below available back ends:

  * `MaxSAT`: Encodes constraints as Max-SAT problem and use Sat4j library to solve.

  * `Lingeling`: Encodes constraints as SAT problem and use Lingeling solver to solve.
  
  * `LogiQL`: Encodes constraints as statements of LogiQL language and use LogicBlox to solve.
  
  * `Z3` with bit vector theory: Encodes constraints as Max-SMT problem with bit vectory theory, and use Z3 library to solve.
  
  
  `MaxSAT` solver is used by default.

* `useGraph`
Specifies whether to separate constraints into multiple components through constraint graph and solve them respectively. The default value is true.

* `solveInParallel`
If constraints are separated by constraint graph, this arguments indicates whether to solve the components in parallel (multithreading). The default value is true. 

* `collectStatistic`
Specifies whether to collect statistic with respect to timing, size of constraints, size of encoding, etc. The default value is false.

For example, generic solver can be invoked through following command:

````
./scripts/inference --mode INFER --checker ostrusted.OsTrustedChecker --solver checkers.inference.solver.GeneralSolver --solverArgs solver=MaxSAT,useGraph=true,collectStatistic=true,solveInParallel=false [List of files]
````

