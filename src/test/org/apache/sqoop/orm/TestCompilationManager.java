package org.apache.sqoop.orm;

import com.cloudera.sqoop.SqoopOptions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TestCompilationManager {

  private CompilationManager compilationManager;

  private SqoopOptions sqoopOptions;

  @Before
  public void before() {
    sqoopOptions = mock(SqoopOptions.class);
    compilationManager = new CompilationManager(sqoopOptions);
  }

  @Test
  public void testPotentialOuterClassNameOfWithPlainClassName() {
    String inputClassName = "FooBar";
    String expectedOutput = inputClassName;

    assertEquals(expectedOutput, compilationManager.potentialOuterClassNameOf(inputClassName));
  }

  @Test
  public void testPotentialOuterClassNameOfWithClassNameEndingWithDollar() {
    String inputClassName = "FooBar$";
    String expectedOutput = "FooBar";

    assertEquals(expectedOutput, compilationManager.potentialOuterClassNameOf(inputClassName));
  }

  @Test
  public void testPotentialOuterClassNameOfWithInnerClassName() {
    String inputClassName = "Foo$Bar";
    String expectedOutput = "Foo";

    assertEquals(expectedOutput, compilationManager.potentialOuterClassNameOf(inputClassName));
  }

  @Test
  public void testPotentialOuterClassNameOfWithOuterClassNameContainingDollarSign() {
    String inputClassName = "Foo$Bar$1";
    String expectedOutput = "Foo$Bar";

    assertEquals(expectedOutput, compilationManager.potentialOuterClassNameOf(inputClassName));
  }

  @Test
  public void testPotentialOuterClassNameOfWithOuterClassNameEndingWithDollarSign() {
    String inputClassName = "FooBar$$1";
    String expectedOutput = "FooBar$";

    assertEquals(expectedOutput, compilationManager.potentialOuterClassNameOf(inputClassName));
  }

  @Test
  public void testIsInnerClassWithOuterClass() {
    String inputSourceFileName = "Foo$Bar.java";
    compilationManager.addSourceFile(inputSourceFileName);
    boolean expectedOutput = false;

    assertEquals(expectedOutput, compilationManager.isInnerClass(inputSourceFileName));
  }

  @Test
  public void testIsInnerClassWithNonExistingClass() {
    String inputSourceFileName = "ThisDoesNotExist.java";
    boolean expectedOutput = false;

    assertEquals(expectedOutput, compilationManager.isInnerClass(inputSourceFileName));
  }

  @Test
  public void testIsInnerClassWithInnerClass() {
    String inputSourceFileName = "Foo$Bar$1.java";
    compilationManager.addSourceFile("Foo$Bar.java");
    boolean expectedOutput = true;

    assertEquals(expectedOutput, compilationManager.isInnerClass(inputSourceFileName));
  }

  @Test
  public void testIsOuterClassWithExistingSourceFile() {
    String inputSourceFileName = "OuterClass.java";
    compilationManager.addSourceFile(inputSourceFileName);
    boolean expectedOutput = true;

    assertEquals(expectedOutput, compilationManager.isOuterClass(inputSourceFileName));
  }

  @Test
  public void testIsOuterClassWithNonExistingSourceFile() {
    String inputSourceFileName = "OuterClass.java";
    boolean expectedOutput = false;

    assertEquals(expectedOutput, compilationManager.isOuterClass(inputSourceFileName));
  }

  @Test
  public void testIncludeFileInJarWithNonClassFile() {
    String inputClassFileName = "FooBar.txt";
    boolean expectedOutput = false;

    assertEquals(expectedOutput, compilationManager.includeFileInJar(inputClassFileName));
  }

  @Test
  public void testIncludeFileInJarWithSourceFilePresent() {
    String inputClassFileName = "FooBar.class";
    boolean expectedOutput = true;
    compilationManager.addSourceFile("FooBar.java");

    assertEquals(expectedOutput, compilationManager.includeFileInJar(inputClassFileName));
  }

  @Test
  public void testIncludeFileInJarWithSourceFileNotPresent() {
    String inputClassFileName = "FooBar.class";
    boolean expectedOutput = false;

    assertEquals(expectedOutput, compilationManager.includeFileInJar(inputClassFileName));
  }

  @Test
  public void testIncludeFileInJarWithOuterClassSourceFilePresent() {
    String inputClassFileName = "FooBar$1.class";
    boolean expectedOutput = true;
    compilationManager.addSourceFile("FooBar.java");

    assertEquals(expectedOutput, compilationManager.includeFileInJar(inputClassFileName));
  }

}