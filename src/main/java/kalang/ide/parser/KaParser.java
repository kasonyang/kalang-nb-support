package kalang.ide.parser;

import kalang.compiler.compile.*;
import kalang.compiler.profile.Profiler;
import kalang.compiler.profile.SpanFormatter;
import kalang.ide.Logger;
import kalang.ide.compiler.ExtendKalangCompiler;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.ide.utils.ClassPathHelper;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.openide.filesystems.FileObject;

import javax.swing.event.ChangeListener;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import static kalang.ide.Logger.log;

public class KaParser extends Parser {

    private KaParserResult result;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        log("task:" + task);
        log("event:" + event);
        String src = snapshot.getText().toString();
        String clsName = ClassPathHelper.getClassName(snapshot.getSource().getFileObject());
        result = new KaParserResult(clsName, snapshot, this);
        parse(snapshot, src, true);
    }

    @Override
    public Result getResult(Task task) {
        return result;
    }

    @Override
    public void cancel() {
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
    }

    private void parse(final Snapshot snapshot, final String src, final boolean collectError) {
        Profiler.getInstance().startProfile();
        final FileObject fo = snapshot.getSource().getFileObject();
        final String clsName = ClassPathHelper.getClassName(snapshot.getSource().getFileObject());
        final ExtendKalangCompiler compiler = NBKalangCompiler.createKalangCompiler(fo);
        DiagnosisHandler dh = new DiagnosisHandler() {
            @Override
            public void handleDiagnosis(Diagnosis ce) {
                Logger.log("handling compile error");
                compiler.stopCompile(compiler.getCompilingPhase());
                if (collectError) {
                    OffsetRange offset = ce.getOffset();
                    KalangError ke = new KalangError(ce, fo, clsName, clsName, ce.getDescription(), offset.startOffset, offset.stopOffset);
                    Logger.log(ce.toString());
                    result.errors.add(ke);
                }
            }
        };
        compiler.setDiagnosisHandler(dh);
        compiler.setCompileTargetPhase(CompilePhase.PHASE_SEMANTIC);
        result.setCompiler(compiler);
        //TODO fix file name
        compiler.addSource(clsName, src, fo.getName());
        //compiler.compile();
        long startTime = System.currentTimeMillis();
        try {
            compiler.compile();
        } catch (Exception ex) {
            Logger.warn(ex);
        }
        long endTime = System.currentTimeMillis();
        log("Compiled(" + (endTime - startTime) + "ms) : " + clsName);
        Profiler.getInstance().stopProfile();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new SpanFormatter().format(Profiler.getInstance().getRootSpan(), os);
        Logger.log(os.toString());
    }

    public static class KaParserResult extends ParserResult {

        private final List<KalangError> errors = new LinkedList();
        private final String className;
        private ExtendKalangCompiler compiler;

        private Snapshot snapshot;
        private final KaParser parser;

        public KaParserResult(String clsName, Snapshot snapshot, KaParser parser) {
            super(snapshot);
            this.snapshot = snapshot;
            this.className = clsName;
            this.parser = parser;
        }

        @Override
        public Snapshot getSnapshot() {
            return snapshot;
        }


        public void setCompiler(ExtendKalangCompiler compiler) {
            this.compiler = compiler;
        }

        public ExtendKalangCompiler getCompiler() {
            return compiler;
        }

        public CompilationUnit getCompilationUnit() {
            return compiler.getCompilationUnit(className);
        }

        public String getClassName() {
            return className;
        }

        @Override
        protected void invalidate() {

        }

        @Override
        public List<KalangError> getDiagnostics() {
            return errors;
        }

        public KaParser getParser() {
            return parser;
        }

    }

}
