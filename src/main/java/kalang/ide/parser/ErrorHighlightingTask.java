package kalang.ide.parser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kalang.ide.parser.KaParser.KaParserResult;
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
    public void run(KaParserResult result, SchedulerEvent event) {
        List<KalangError> errors = result.getDiagnostics();
        List<ErrorDescription> errDescList = new ArrayList<ErrorDescription>();
        if (errors != null) {
            Document doc = result.getSnapshot().getSource().getDocument(false);
            for (KalangError e : errors) {
                String msg = e.getDescription();
                if (msg == null) {
                    msg = "";
                }
                int start = e.getStartPosition();
                int stop = e.getEndPosition();
                try {
                    ErrorDescription errorDesc = ErrorDescriptionFactory.createErrorDescription(
                            transSeverity(e.getSeverity()),
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
            HintsController.setErrors(doc, "kalang", errDescList);
        }
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }

    private Severity transSeverity(org.netbeans.modules.csl.api.Severity cslSeverity) {
        switch (cslSeverity) {
            case INFO:
                return Severity.HINT;
            case WARNING:
                return Severity.WARNING;
            case ERROR:
            case FATAL:
            default:
                return Severity.ERROR;
        }
    }

}