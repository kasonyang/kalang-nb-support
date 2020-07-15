package kalang.ide.compiler.complete;

import kalang.compiler.core.ObjectType;

import javax.annotation.Nullable;

/**
 * @author KasonYang
 */
public class MemberCompletion implements Completion {

    private ObjectType ownerType;

    @Nullable
    private Boolean staticMember;

    public MemberCompletion(ObjectType ownerType, @Nullable Boolean staticMember) {
        this.ownerType = ownerType;
        this.staticMember = staticMember;
    }

    public ObjectType getOwnerType() {
        return ownerType;
    }

    @Nullable
    public Boolean getStaticMember() {
        return staticMember;
    }
}
