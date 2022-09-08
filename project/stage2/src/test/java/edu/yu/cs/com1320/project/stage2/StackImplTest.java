package edu.yu.cs.com1320.project.stage2; 
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.StackImpl;
import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals; 

public class StackImplTest{
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

    
}
