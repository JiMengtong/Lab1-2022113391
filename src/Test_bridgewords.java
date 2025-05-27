import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * 针对 Lab1 类的 queryBridgeWords 方法的黑盒测试类.
 */
public class Test_bridgewords {
  private Lab1 lab1;
  
  /**
   * 初始化测试环境，构建有向图.
   *
   * @throws IOException 如果文件读取失败
   */
  @Before
  public void setUp() throws IOException {
    lab1 = new Lab1();
    lab1.buildGraph("Easy Test.txt");
  }
  
  /**
   * 测试存在桥接词的情况：scientist -> carefully -> analyzed.
   */
  @Test
  public void testBridgeWordsExistBetweenScientistAndAnalyzed() {
    String result = lab1.queryBridgeWords("scientist", "analyzed");
    assertEquals(
        "The bridge word from \"scientist\" to \"analyzed\" is: carefully.",
        result
    );
  }
  
  /**
   * 测试存在桥接词的情况：report -> and -> shared.
   */
  @Test
  public void testBridgeWordsExistBetweenReportAndShared() {
    String result = lab1.queryBridgeWords("report", "shared");
    assertEquals(
        "The bridge word from \"report\" to \"shared\" is: and.",
        result
    );
  }
  
  /**
   * 测试不存在桥接词的情况：team -> data.
   */
  @Test
  public void testNoBridgeWordsBetweenTeamAndData() {
    String result = lab1.queryBridgeWords("team", "data");
    assertEquals(
        "No bridge word from \"team\" to \"data\"!",
        result
    );
  }
  
  /**
   * 测试输入单词不存在的情况：apple -> data.
   */
  @Test
  public void testWord1NotInGraph() {
    String result = lab1.queryBridgeWords("apple", "data");
    assertEquals(
        "No apple or data in the graph!",
        result
    );
  }
  
  /**
   * 测试输入单词不存在的情况：data -> apple.
   */
  @Test
  public void testWord2NotInGraph() {
    String result = lab1.queryBridgeWords("data", "apple");
    assertEquals(
        "No data or apple in the graph!",
        result
    );
  }
  
  /**
   * 测试非法输入（含非字母字符）：123 -> data.
   */
  @Test
  public void testInvalidInputWithNonAlphabeticCharacters() {
    String result = lab1.queryBridgeWords("123", "data");
    assertEquals(
        "No 123 or data in the graph!",
        result
    );
  }
}