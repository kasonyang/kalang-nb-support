package kalang.ide.project;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

import java.io.IOException;

/**
 * @author KasonYang
 */
@ServiceProvider(service = ProjectFactory.class)
public class KlShellProjectFactory implements ProjectFactory {


    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(KlShellProject.PROJECT_FILE) != null;
    }

    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        return isProject(projectDirectory) ? new KlShellProject(projectDirectory, state) : null;
    }

    @Override
    public void saveProject(Project project) throws IOException, ClassCastException {

    }

}
