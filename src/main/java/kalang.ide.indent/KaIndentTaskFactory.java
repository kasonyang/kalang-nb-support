package kalang.ide;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.IndentTask;

@MimeRegistration(mimeType="text/x-kalang",service=IndentTask.Factory.class)
public class KaIndentTaskFactory implements IndentTask.Factory {

    @Override
    public IndentTask createTask(Context context) {
        return new KaIndentTask(context);
    }

}