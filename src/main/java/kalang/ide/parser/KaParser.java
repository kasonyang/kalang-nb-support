package kalang.ide.parser;

import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.compile.Diagnosis;
import kalang.compiler.compile.DiagnosisHandler;
import kalang.compiler.compile.KalangCompiler;
import kalang.compiler.compile.OffsetRange;

import kalang.ide.Logger;
import static kalang.ide.Logger.log;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.ide.utils.ClassPathHelper;
import org.antlr.v4.runtime.tree.ParseTree;
import org.netbeans.modules.csl.spi.GsfUtilities;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.openide.filesystems.FileObject;

public class KaParser extends Parser {

    private Snapshot snapshot;

    //private KalangParser parser;
    //private SourceParser srcParser;
    private KaParserResult result;

    private List<KalangError> errors = new LinkedList();

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        log("task:" + task);
        log("event:" + event);
        this.snapshot = snapshot;
        String src = snapshot.getText().toString();
        int srcLen = src.length();
        int caretOffset = GsfUtilities.getLastKnownCaretOffset(snapshot, event);
        log("last caretOffset:" + caretOffset);
        int rowStart;
        try {
            rowStart = GsfUtilities.getRowStart(src, caretOffset);
        } catch (BadLocationException ex) {
            rowStart = -1;
        }
        int lastDotOffset = -1;
        if (rowStart > 0) {
            for (int i = caretOffset; i >= rowStart; i--) {
                if (i >= srcLen) {
                    continue;
                }
                if (src.charAt(i) == '.') {
                    lastDotOffset = i;
                    break;
                }
            }
        }
        log("last dot offset:" + lastDotOffset);
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
        final FileObject fo = snapshot.getSource().getFileObject();
        final String clsName = ClassPathHelper.getClassName(snapshot.getSource().getFileObject());
        final KalangCompiler compiler = NBKalangCompiler.createKalangCompiler(fo);
        DiagnosisHandler dh = new DiagnosisHandler() {
          @Override
          public void handleDiagnosis(Diagnosis ce) {
            Logger.log("handling compile error");
            compiler.stopCompile(compiler.getCompilingPhase());
            if(collectError){
                OffsetRange offset = ce.getOffset();
                KalangError ke = new KalangError(ce, fo, clsName, clsName, ce.getDescription(),offset.startOffset,offset.stopOffset);
                Logger.log(ce.toString());
                result.errors.add(ke);
            }
          }
        };
        compiler.setDiagnosisHandler(dh);
        result.setCompiler(compiler);
        //TODO fix file name
        compiler.addSource(clsName, src , fo.getName());
        log("Compiling " + clsName);
        try {
            compiler.compile();
        } catch (Exception ex) {
            Logger.warn(ex);
        }
    }

    public static class KaParserResult extends ParserResult {

        private boolean valid = true;

        int caretOffset = -1;

        //KalangParser parser;
        public final List<KalangError> errors = new LinkedList();
//        private final CompilationUnit sourceParser;
//        private final SemanticAnalyzer typeChecker;
        private final String className;
        private KalangCompiler compiler;

        private Snapshot snapshot;
//        private final CommonTokenStream tokenStream;
//        private final TokenNavigator tokenNavigator;
//        private final ParseTreeNavigator parseTreeNavigator;
        private final KaParser parser;

        public KaParserResult(String clsName, Snapshot snapshot, KaParser parser) {
            super(snapshot);
            this.snapshot = snapshot;
            this.className = clsName;
            //this.sourceParser = klCpl.getCompilationUnit(clsName);
//            this.tokenStream = sourceParser.getTokenStream();
//            this.tokenNavigator = new TokenNavigator(this.tokenStream.getTokens().toArray(new Token[0]));
//            parseTreeNavigator = new ParseTreeNavigator(this.sourceParser.getAstBuilder().getParseTree());
//            typeChecker = sourceParser.getSemanticAnalyzer();
            this.parser = parser;
        }

        public int getCaretOffset() {
            return caretOffset;
        }

        @Override
        public Snapshot getSnapshot() {
            return snapshot;
        }

//        public CommonTokenStream getTokenStream() {
//            return tokenStream;
//        }
//
//        public TokenNavigator getTokenNavigator() {
//            return tokenNavigator;
//        }
//
//        public ParseTreeNavigator getParseTreeNavigator() {
//            return parseTreeNavigator;
//        }

        public void setCompiler(KalangCompiler compiler) {
            this.compiler = compiler;
        }

        public KalangCompiler getCompiler() {
            return compiler;
        }
        
        public CompilationUnit getCompilationUnit(){
            return compiler.getCompilationUnit(className);
        }

//        public CompilationUnit getSourceParser() {
//            return sourceParser;
//        }

        public String getClassName() {
            return className;
        }

//        public int getTokenIndexByCaretOffset(int offset) {
//            tokenNavigator.move(offset);
//            return tokenNavigator.getCurrentTokenIndex();
//        }

        String tree2String(ParseTree tree) {
            String str = tree.getText() + " ";
            str += tree.getClass();
            str += "{";
            for (int i = 0; i < tree.getChildCount(); i++) {
                str += tree.getChild(i).getClass() + " ";
            }
            str += "}";
            return str;
        }

//        public List<KalangError> getErrors() {
//            return errors;
//        }
        @Override
        protected void invalidate() {
            valid = false;
        }

        @Override
        public List<KalangError> getDiagnostics() {
            return errors;
        }

        public int getAnchorOffset() {
            return caretOffset;
        }

//        public SemanticAnalyzer getTypeChecker() {
//            return typeChecker;
//        }

        public KaParser getParser() {
            return parser;
        }

    }

}
