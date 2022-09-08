package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.StackImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals; 
import java.net.URI;
import java.net.URISyntaxException;


public class StackImplTest{

    private StackImpl<Command> stack;
    private Command cmd1;
    private Command cmd2;

    @BeforeEach
    public void initVariables() throws URISyntaxException {
        this.stack = new StackImpl<Command>();
        //uri & cmd 1
        URI uri1 = new URI("http://www.test1.net");
        this.cmd1 = new Command(uri1, target ->{
            return target.equals(uri1);
        });
        //uri & cmd 2
        URI uri2 = new URI("http://www.test2.net");
        this.cmd2 = new Command(uri2, target ->{
            return target.equals(uri2);
        });
        this.stack.push(this.cmd1);
        this.stack.push(this.cmd2);
    }

    @Test
    public void pushAndPopTest(){
        Command pcmd = this.stack.pop();
        assertEquals(this.cmd2,pcmd,"first pop should've returned second command");
        pcmd = this.stack.pop();
        assertEquals(this.cmd1,pcmd,"second pop should've returned first command");
    }

    @Test
    public void peekTest(){
        Command pcmd = this.stack.peek();
        assertEquals(this.cmd2,pcmd,"first peek should've returned second command");
        pcmd = this.stack.pop();
        assertEquals(this.cmd2,pcmd,"first pop after peek should've returned the second command");

        pcmd = this.stack.peek();
        assertEquals(this.cmd1,pcmd,"second peek should've returned first command");
        pcmd = this.stack.pop();
        assertEquals(this.cmd1,pcmd,"second pop should've returned first command");
    }
    @Test
    public void sizeTest(){
        assertEquals(2,this.stack.size(),"two commands should be on the stack");
        this.stack.peek();
        assertEquals(2,this.stack.size(),"peek should not have affected the size of the stack");
        this.stack.pop();
        assertEquals(1,this.stack.size(),"one command should be on the stack after one pop");
        this.stack.peek();
        assertEquals(1,this.stack.size(),"peek still should not have affected the size of the stack");
        this.stack.pop();
        assertEquals(0,this.stack.size(),"stack should be empty after 2 pops");
    }

    @Test
    public void popTest(){
        Stack<Integer> stack = new StackImpl<>();
        stack.push(1);
        assertEquals(1, stack.peek());
        assertEquals(1, stack.size());

        stack.push(2);
        assertEquals(2, stack.peek());
        assertEquals(2, stack.size());
        assertEquals(2, stack.pop());

        assertEquals(1, stack.pop());

        assertEquals(null, stack.pop());
        assertEquals(0, stack.size());
        assertEquals(null, stack.peek());
    }

    @Test
    public void testSize(){
        Stack<Integer> stack = new StackImpl<>();
        stack.push(1);
        stack.push(2);
        stack.push(1);
        stack.push(15);
        assertEquals(4, stack.size());
        stack.pop();
        assertEquals(3, stack.size());
        stack.pop();
        assertEquals(2, stack.size());
        stack.pop();
        assertEquals(1, stack.size());
        stack.pop();
        assertEquals(0, stack.size());
    }

    @Test
    public void testPop(){
        Stack<Integer> stack = new StackImpl<>();
        stack.push(1);
        stack.push(2);
        stack.push(1);
        stack.push(15);
        assertEquals(15, stack.pop());
        assertEquals(1, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertEquals(null, stack.pop());
    }
   
}