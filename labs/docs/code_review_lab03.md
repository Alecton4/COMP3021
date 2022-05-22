# Code Review lab03

## Task 01

### Difficulties

The difficulties I met in lab03 is the implementation of `searchNotes()` function. There are two main difficulties:

1. How to parse the individual keywords correctly when they are used together with `AND` and `OR`.
2. After retrieving the keywords, how to search in notes.

### Solutions

1. I maintain two "stacks" which holds keywords with `AND` relationship and keywords with `OR` relationship respectively. The `AND` stack is simply a string stack, but the `OR` stack is a string array stack because we have to know which keywords are in the same `OR` group (for instance, in sentence `A OR B OR C AND D OR E`, `A`, `B` and `C` are in one `OR` group, `D` and `E` are in another group).
2. I write a `StringHelper` class to help me search target string in source string. Actually using a single function provided by java library is already enough to do the work, but I wrote this class deliberately to make the program run faster because calling a function is somewhat expensive.

## Task 02

To accomplish this task, I think we can do the following:

1. Parse the source string and store separated substrings in "HashMap", in which we set the "key" as substrings and "value" as the occurrence.
2. After storing all the substrings, we iterate through the hashmap and find the corresponding targets.
