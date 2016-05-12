package kalang.ide.parser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kalang.antlr.KalangParser;
import kalang.ide.parser.KaParser.KaParserResult;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.util.Exceptions;

public class ErrorHighlightingTask extends ParserResultTask<KaParserResult> {

    @Override
    public void run (KaParserResult result, SchedulerEvent event) {
            List<KalangError> errors = result.getDiagnostics();
            List<ErrorDescription> errDescList = new ArrayList<ErrorDescription> ();
            if(errors!=null){
                Document doc = result.getSnapshot().getSource().getDocument(false);
                for(KalangError e:errors){
//                    String excepted = e.getExpectedTokens().toString(KalangParser.VOCABULARY);
//                    Token errTk = e.getOffendingToken();
//                    String msg = errTk.getText() + " is unexcepted ,excepted " + excepted;
//                    int start = errTk.getStartIndex();
//                    int stop = errTk.getStopIndex();
                    String msg = e.getDescription();
                    int start = e.getStartPosition();
                    int stop = e.getEndPosition();
                    try{
                        ErrorDescription errorDesc = ErrorDescriptionFactory.createErrorDescription(
                            Severity.ERROR,
                            msg,
                            doc,
                            doc.createPosition(start),
                            doc.createPosition(stop)
                        );
                        errDescList.add(errorDesc);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                HintsController.setErrors (doc, "kalang", errDescList);
            }
    }

    @Override
    public int getPriority () {
        return 50;
    }

    @Override
    public Class getSchedulerClass () {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel () {
    }

}