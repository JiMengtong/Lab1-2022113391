import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * 实验内容一：基于大模型的编程
 * 实现功能：
 * 1. 根据文本生成有向图并展示
 * 2. 查询桥接词
 * 3. 根据桥接词生成新文本
 * 4. 计算两词之间最短路径
 * 5. 计算PageRank
 * 6. 随机游走
 * 7. 将有向图保存为图像文件（依赖Graphviz）
 */
public class Lab1 {
  // 图的内部表示：邻接表，key->Map<neighbor, weight>
  private Map<String, Map<String, Integer>> graph = new HashMap<>();
  private static final double DAMPING = 0.85;
  
  /**
   * 程序主入口，提供命令行交互界面以调用图操作功能.
   *
   * @param args 命令行参数（未使用）
   */
  public static void main(String[] args) {
    Lab1 app = new Lab1();
    // Scanner sc = new Scanner(System.in);
    Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    String path = "Easy Test.txt";
    try {
      app.buildGraph(path);
    } catch (IOException e) {
      System.err.println("读取文件出错：" + e.getMessage());
      return;
    }
    
    boolean exit = false;
    while (!exit) {
      System.out.println("请选择功能：");
      System.out.println("1. 展示有向图");
      System.out.println("2. 查询桥接词");
      System.out.println("3. 根据桥接词生成新文本");
      System.out.println("4. 计算两词最短路径");
      System.out.println("5. 计算PageRank");
      System.out.println("6. 随机游走");
      System.out.println("7. 将有向图保存为图像文件");
      System.out.println("0. 退出");
      System.out.print("输入编号：");
      String choice = sc.nextLine();
      switch (choice) {
        case "1":
          app.showDirectedGraph();
          break;
        case "2":
          System.out.print("输入word1：");
          String w1 = sc.nextLine().toLowerCase();
          System.out.print("输入word2：");
          String w2 = sc.nextLine().toLowerCase();
          System.out.println(app.queryBridgeWords(w1, w2));
          break;
        case "3":
          System.out.print("输入新文本：");
          String text = sc.nextLine();
          System.out.println(app.generateNewText(text));
          break;
        case "4":
          System.out.print("输入word1：");
          String s1 = sc.nextLine().toLowerCase();
          System.out.print("输入word2：");
          String s2 = sc.nextLine().toLowerCase();
          System.out.println(app.calcShortestPath(s1, s2));
          break;
        case "5":
          System.out.print("输入单词：");
          String prWord = sc.nextLine().toLowerCase();
          Double pr = app.calPageRank(prWord);
          System.out.printf("%s 的PageRank值：%.4f%n", prWord, pr);
          break;
        case "6":
          System.out.println(app.randomWalk());
          break;
        case "7":
          String dotPath = "graph.dot";
          String imgPath = "graph.png";
          try {
            app.saveGraphAsImage(dotPath, imgPath);
          } catch (IOException | InterruptedException e) {
            System.err.println("保存图像失败：" + e.getMessage());
          }
          break;
        case "0":
          exit = true;
          break;
        default:
          System.out.println("无效输入，请重新选择。");
      }
    }
    sc.close();
  }
  
  /**
   * 构建有向图：读取文件、清洗文本并生成邻接表.
   *
   * @param filePath 文件路径
   * @throws IOException 文件读取异常
   */
  void buildGraph(String filePath) throws IOException {
    List<String> words = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.replaceAll("[^A-Za-z]", " ").toLowerCase();
        String[] tokens = line.split("\\s+");
        for (String tok : tokens) {
          if (!tok.isEmpty()) {
            words.add(tok);
          }
        }
      }
    }
    for (int i = 0; i < words.size() - 1; i++) {
      String a = words.get(i);
      String b = words.get(i + 1);
      graph.putIfAbsent(a, new HashMap<>());
      Map<String, Integer> edges = graph.get(a);
      edges.put(b, edges.getOrDefault(b, 0) + 1);
      graph.putIfAbsent(b, new HashMap<>());
    }
  }
  
  /**
   * 展示有向图（邻接表形式）.
   */
  public void showDirectedGraph() {
    System.out.println("有向图邻接表（节点 -> [邻居: 权重,...]）：");
    graph.forEach((node, edges) -> System.out.println(node + " -> " + edges));
  }
  
  /**
   * 查询桥接词.
   *
   * @param word1 起始词
   * @param word2 目标词
   * @return 桥接词结果描述
   */
  public String queryBridgeWords(String word1, String word2) {
    if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
      return String.format("No %s or %s in the graph!", word1, word2);
    }
    Set<String> bridges = new HashSet<>();
    graph.get(word1).keySet().stream()
        .filter(mid -> graph.get(mid).containsKey(word2))
        .forEach(bridges::add);
    if (bridges.isEmpty()) {
      return String.format("No bridge word from \"%s\" to \"%s\"!", word1, word2);
    }
<<<<<<< HEAD

    /**
     * 计算PageRank
     */
    public Double calPageRank(String word) {
        int N = graph.size();
        Map<String, Double> pr = new HashMap<>(),
                newPr = new HashMap<>();
        graph.keySet().forEach(n -> pr.put(n, 1.0 / N));

        for (int iter = 0; iter < 100; iter++) {
            double danglingSum = graph.keySet().stream()
                    .filter(u -> graph.get(u).isEmpty())
                    .mapToDouble(pr::get)
                    .sum();

            for (String node : graph.keySet()) {
                double sumIn = graph.keySet().stream()
                        .filter(u -> graph.get(u).containsKey(node))
                        .mapToDouble(u -> pr.get(u) / graph.get(u).size())
                        .sum();
                double danglingContribution = danglingSum / N;
                newPr.put(node,
                        (1 - DAMPING) / N
                                + DAMPING * (sumIn + danglingContribution));
            }
            pr.putAll(newPr);
        }
        return pr.getOrDefault(word, 0.0);
=======
    return String.format("The bridge word from \"%s\" to \"%s\" is: %s.",
        word1, word2, String.join(", ", bridges));
  }
  
  /**
   * 生成新文本：在相邻词间插入随机桥接词.
   *
   * @param inputText 输入文本
   * @return 生成的新文本
   */
  public String generateNewText(String inputText) {
    String cleaned = inputText.replaceAll("[^A-Za-z\\s]", " ").toLowerCase();
    String[] tokens = cleaned.split("\\s+");
    // Random rand = new Random();
    SecureRandom rand = new SecureRandom();
    List<String> output = new ArrayList<>();
    for (int i = 0; i < tokens.length - 1; i++) {
      String w1 = tokens[i];
      String w2 = tokens[i + 1];
      output.add(w1);
      Set<String> set = new HashSet<>();
      graph.getOrDefault(w1, Collections.emptyMap()).keySet().stream()
          .filter(mid -> graph.get(mid).containsKey(w2))
          .forEach(set::add);
      if (!set.isEmpty()) {
        output.add(new ArrayList<>(set).get(rand.nextInt(set.size())));
      }
>>>>>>> Lab3b
    }
    output.add(tokens[tokens.length - 1]);
    return String.join(" ", output);
  }
  
  /**
   * 计算两词之间的最短路径（Dijkstra）.
   *
   * @param word1 起始词
   * @param word2 目标词
   * @return 最短路径描述
   */
  public String calcShortestPath(String word1, String word2) {
    if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
      return String.format("%s 或 %s 不在图中！", word1, word2);
    }
    Map<String, Integer> dist = new HashMap<>();
    Map<String, String> prev = new HashMap<>();
    graph.keySet().forEach(n -> dist.put(n, Integer.MAX_VALUE));
    dist.put(word1, 0);
    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
    pq.add(word1);
    while (!pq.isEmpty()) {
      String u = pq.poll();
      if (u.equals(word2)) {
        break;
      }
      for (Map.Entry<String, Integer> e : graph.get(u).entrySet()) {
        String v = e.getKey();
        int w = e.getValue();
        int alt = dist.get(u) + w;
        if (alt < dist.get(v)) {
          dist.put(v, alt);
          prev.put(v, u);
          pq.remove(v);
          pq.add(v);
        }
      }
    }
    if (!prev.containsKey(word2) && !word1.equals(word2)) {
      return String.format("%s 到 %s 不可达。", word1, word2);
    }
    List<String> path = new LinkedList<>();
    for (String cur = word2; cur != null; cur = prev.get(cur)) {
      path.add(0, cur);
    }
    return String.format("最短路径：%s，长度：%d", String.join("->", path), dist.get(word2));
  }
  
  /**
   * 计算PageRank.
   *
   * @param word 目标词
   * @return PageRank值
   */
  public Double calPageRank(String word) {
    int n = graph.size();
    Map<String, Double> pr = new HashMap<>();
    Map<String, Double> newPr = new HashMap<>();
    graph.keySet().forEach(node -> pr.put(node, 1.0 / n));
    
    for (int iter = 0; iter < 100; iter++) {
      // 1. 计算悬挂节点总质量
      double danglingSum = graph.keySet().stream()
          .filter(u -> graph.get(u).isEmpty())
          .mapToDouble(pr::get)
          .sum();
      
      // 2. 迭代更新每个节点
      for (String node : graph.keySet()) {
        double sumIn = graph.keySet().stream()
            .filter(u -> graph.get(u).containsKey(node))
            .mapToDouble(u -> pr.get(u) / graph.get(u).size())
            .sum();
        double danglingContribution = danglingSum / n;
        newPr.put(node,
            (1 - DAMPING) / n
                + DAMPING * (sumIn + danglingContribution));
      }
      pr.putAll(newPr);
    }
    return pr.getOrDefault(word, 0.0);
  }
  
  /**
   * 随机游走.
   *
   * @return 游走路径
   */
  public String randomWalk() {
    if (graph.isEmpty()) {
      return "图为空。";
    }
    // Random rand = new Random();
    SecureRandom rand = new SecureRandom();
    String current = new ArrayList<>(graph.keySet()).get(rand.nextInt(graph.size()));
    Set<String> seen = new HashSet<>();
    List<String> path = new ArrayList<>();
    path.add(current);
    while (true) {
      Map<String, Integer> outs = graph.get(current);
      if (outs.isEmpty()) {
        break;
      }
      String next = new ArrayList<>(outs.keySet()).get(rand.nextInt(outs.size()));
      String edge = current + "->" + next;
      if (seen.contains(edge)) {
        break;
      }
      seen.add(edge);
      current = next;
      path.add(current);
    }
    return String.join(" ", path);
  }
  
  /**
   * 将有向图保存为图像文件，依赖Graphviz的dot命令.
   *
   * @param dotFilePath DOT文件路径
   * @param imgFilePath 图像文件路径
   * @throws IOException 文件写入异常
   * @throws InterruptedException 进程中断异常
   */
  public void saveGraphAsImage(String dotFilePath, String imgFilePath)
      throws IOException, InterruptedException {
    // 写DOT文件
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFilePath))) {
      writer.write("digraph G {\n");
      for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
        String src = entry.getKey();
        for (Map.Entry<String, Integer> e : entry.getValue().entrySet()) {
          writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%d\"];%n",
              src, e.getKey(), e.getValue()));
        }
      }
      writer.write("}\n");
    }
    // 调用Graphviz生成PNG
    Process p = Runtime.getRuntime().exec(
        new String[] {"dot", "-Tpng", dotFilePath, "-o", imgFilePath}
    );
    int code = p.waitFor();
    if (code != 0) {
      System.err.println("Graphviz 生成失败，退出码=" + code);
    } else {
      System.out.println("图像已保存到：" + imgFilePath);
    }
  }
}