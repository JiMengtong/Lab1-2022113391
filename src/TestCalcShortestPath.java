import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * 针对 Lab1 类的 calcShortestPath 方法的白盒测试类.
 */
public class TestCalcShortestPath {
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
   * 测试路径不存在的情况：输入单词不在图中.
   */
  @Test
  public void testWordsNotInGraph() {
    String result = lab1.calcShortestPath("apple", "data");
    assertEquals("apple 或 data 不在图中！", result);
  }
  
  /**
   * 测试路径不可达的情况：team -> data.
   */
  @Test
  public void testUnreachablePath() {
    String result = lab1.calcShortestPath("again", "data");
    assertEquals("again 到 data 不可达。", result);
  }
  
  /**
   * 测试直接路径的情况：scientist -> carefully.
   */
  @Test
  public void testDirectPath() {
    String result = lab1.calcShortestPath("scientist", "carefully");
    assertEquals("最短路径：scientist->carefully，长度：1", result);
  }
  
  /**
   * 测试通过中间节点的路径：scientist -> carefully -> data.
   */
  @Test
  public void testIndirectPath() {
    String result = lab1.calcShortestPath("scientist", "data");
    assertEquals("最短路径：scientist->analyzed->the->data，长度：3", result);
  }
  
  /**
   * 测试起点和终点相同的情况：data -> data.
   */
  @Test
  public void testSameNode() {
    String result = lab1.calcShortestPath("data", "data");
    assertEquals("最短路径：data，长度：0", result);
  }
}