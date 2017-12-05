import JBibtexParser.typemanager.definitions.IDefinition;
import com.JBibtexParser.Parser;
import com.JBibtexParser.bibliography.Bibliography;
import com.JBibtexParser.bibliography.IBibliographyManager;
import com.JBibtexParser.typemanager.IEntryTypesManager;
import com.JBibtexParser.typemanager.definitions.BibtexDefinition;
import com.JBibtexParser.util.exceptions.ParseErrorException;
import com.JBibtexParser.verification.SimpleVerifier;
import com.JBibtexParser.verification.VerificationMode;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] params) {

        //a simple constructor for creating parser with default componenents and a file reader
        Parser parser = new Parser("samplefile.bib");

        IBibliographyManager bibliographyManager;

        //parse the file
        try {
            bibliographyManager = parser.parse();
        } catch (ParseErrorException e) {
            System.out.print(e.getMessage());
            return;
        }

        //create a verifier
        IDefinition bibtexDefinition = new BibtexDefinition();
        SimpleVerifier verifier = new SimpleVerifier(bibtexDefinition, VerificationMode.FULL_VERIFY);

        //load the entire bibliography into mainBibliography
        Bibliography mainBibliography = bibliographyManager.getBibliography();

        //verify the bibliography
        verifier.verifyBibliography(mainBibliography);

        //print the verification report
        System.out.println(verifier.getVerificationReport().toString());

        //print out all entries
        System.out.print(mainBibliography.toString());
        //print out all entries containing the word mathematics
        System.out.print(bibliographyManager.findEntriesContainingWords("mathematics").getBibliography().toString());


    }

}
