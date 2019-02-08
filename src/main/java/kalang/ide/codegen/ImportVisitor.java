package kalang.ide.codegen;

import java.util.ArrayList;
import java.util.List;
import kalang.compiler.antlr.KalangParser;
import kalang.compiler.antlr.KalangParserBaseVisitor;

/**
 *
 * @author Kason Yang
 */
public class ImportVisitor extends KalangParserBaseVisitor<Object> {

    final List<String> imported = new ArrayList();

    int importStart = -1;

    int importEnd = -1;

    @Override
    public Object visitImportDecl(KalangParser.ImportDeclContext ctx) {
        if (importStart < 0) {
            importStart = ctx.getStart().getStartIndex();
        }
        importEnd = ctx.getStop().getStopIndex();
        imported.add(ctx.name.getText());
        return super.visitImportDecl(ctx);
    }

    /**
     * get imported class(simple name)
     * @return 
     */
    public List<String> getImported() {
        return imported;
    }

    public int getImportStart() {
        return importStart;
    }

    public int getImportEnd() {
        return importEnd;
    }

}
