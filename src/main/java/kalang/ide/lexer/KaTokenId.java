package kalang.ide.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

public class KaTokenId implements TokenId {

    private final String name;
    private final String primaryCategory;
    private final int id;

    KaTokenId(
            String name,
            String primaryCategory,
            int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    public static Language<KaTokenId> getLanguage() {
        return new KaLanguageHierarchy().language();
    }

    @Override
    public String toString() {
        return "KaTokenId{" + name + ", " + primaryCategory + ", " + id + '}';
    }
    
    
    
}