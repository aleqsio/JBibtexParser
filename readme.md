# JBibtexParser


This is a project aiming to provide an extensible java library for parsing bibtex or bibtex-similar files.

The goal was to make it very easy to use, but almost all parts of the project can be replaced with your own implementations simply by passing a custom class to the parser constructor.

## Getting Started

Add the provided JBibtexParser.jar to your project, and include it in your dependencies.
Afterwards just instantiate the parser and call Parser.parse(). It will return an IBibliograpyManager which you can use to filter entries by types, fields or simply keywords

## Example use

A simple constructor for creating parser with default componenents and a file reader:

        Parser parser = new Parser("samplefile.txt");

Parse the file:

        IBibliographyManager bibliographyManager;
        try {
            bibliographyManager = parser.parse();
        } catch (ParseErrorException e) {
            System.out.print(e.getMessage());
            return;
        }

Load the entire bibliography into a Bibliography object:

        Bibliography mainBibliography = bibliographyManager.getBibliography();

Print out all entries:

        System.out.print(mainBibliography.toString());
Print out all entries containing which contain both of the words 'mathematics' and 'theories'. You can chain filters:

        System.out.print(bibliographyManager.findEntriesContainingWords("mathematics").findEntriesContainingWords("theories")
        .getBibliography().toString());
print out all entries where author matches a regex ```.\*shelah.*```:

          System.out.println(bibliographyManager.findFieldsOfValue(parser.getEntryTypesManager().getField("author"),".*shelah.*").getBibliography());

## Running the tests

There are some (26) Junit5 tests written for the most bug-prone components, but full test coverage is definitly on the TODO list.
You can find them in the tests package  and run them as normal.

## Authors

* **Aleksander Mikucki** - [mailto:aleqsio](mailto://mikucki@gmail.com)

## License

This project is licensed under the MIT License - see the [license.md](license.md) file for details
