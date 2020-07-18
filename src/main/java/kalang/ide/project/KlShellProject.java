package kalang.ide.project;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author KasonYang
 */
public class KlShellProject implements Project {

    public final static String PROJECT_FILE = "kalangsh.options";

    private final FileObject projectDir;

    private final ProjectState state;

    private Lookup lookup;

    private final KlShellProjectOpenedHook projectOpenedHook;

    private final KlShellClassProvider classPathProvider;

    KlShellProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
        projectOpenedHook = new KlShellProjectOpenedHook(this);
        classPathProvider = new KlShellClassProvider();
    }

    @Override
    public FileObject getProjectDirectory() {
        return this.projectDir;
    }

    public String getName() {
        return projectDir.getName();
    }

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = Lookups.fixed(
                    new Info(),
                    projectOpenedHook,
                    classPathProvider,
                    CommonProjectActions.closeProjectAction()
            );
        }
        return lookup;
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String ICON = "kalang/ide/kalang.png";

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(ICON));
        }

        @Override
        public Project getProject() {
            return KlShellProject.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {

        }
    }

}
