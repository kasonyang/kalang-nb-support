/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.test;

import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.antlr.KalangParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class RecoverTest {
    
    public RecoverTest() {
        
    }
    @Test
    public void test(){
                //String src = "main().";
                String src = "void main(){main().}";
        KalangLexer lexer = new KalangLexer(new ANTLRInputStream(src));
        CommonTokenStream tokens = new CommonTokenStream(lexer)/*{
            @Override
            public Token get(int i) {
                Token tk = super.get(i);
                Logger.log("token "+i + ":" + tk);
                return tk;
            }
            
        }*/;
        KalangParser parser = new KalangParser(tokens);
        parser.setErrorHandler(new DefaultErrorStrategy(){
                    private boolean recovering;
                    @Override
                    public Token recoverInline(Parser recognizer) throws RecognitionException {
                        System.out.println("on recoverInline");
                        return super.recoverInline(recognizer); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void recover(Parser recognizer, RecognitionException e) {
                   System.out.println("on recover");
                   if(true) return;
                   if(recovering){
                       super.recover(recognizer, e);
                   }else{
                       recovering = true;
                       KalangParser parser =  (KalangParser) recognizer;
                       super.recover(recognizer, e);
                       //parser.expression();
                   }
                   
                   
                   //parser.setErrorHandler(null);
                   //if(inErrorRecoveryMode(recognizer))
                   //super.recover(recognizer, e);
//            recognizer.consume();
//                        Token lt1 = recognizer.getInputStream().LT(1);
//                        System.out.println("on recover");
//                        Token lt2 = recognizer.getInputStream().LT(2);
//                        System.out.println(lt1.getText() + lt2.getText());
//                        if(lt2.getText().equals(".")){
//                            recognizer.consume();
//                            recognizer.consume();
//                        }else{
//                            super.recover(recognizer, e);
//                        }
                    }       

                    @Override
                    public void sync(Parser recognizer) throws RecognitionException {
                        System.out.println("on syn:"+recognizer.getInputStream().LT(1).getText()+this.inErrorRecoveryMode(recognizer));
                        super.sync(recognizer); //To change body of generated methods, choose Tools | Templates.
                    }
                    
        });
        //parser.expression();
        KalangParser.MethodDeclContext m = parser.methodDecl();
        System.out.println(m.toStringTree());
        
    }
}
