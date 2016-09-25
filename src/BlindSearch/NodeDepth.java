package BlindSearch;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;


public class NodeDepth extends Node {

    private static final LinkedList<NodeDepth> QUEUE = new LinkedList<>();
    private static final HashSet<State> STATES = new HashSet<>();

    NodeDepth(int depth, Node parent, int cost, char action, State state) {
        super(depth, parent, cost, action, state);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        createFirstNode();
        while (nextStep()) {
            if (!flag) {
                flag = "run".equalsIgnoreCase(reader.readLine());
            }
        }
    }

    /**
     * Создание начальной вершины
     */
    private static void createFirstNode() {
        NodeDepth node = new NodeDepth(0, null, 0, '0', State.FIRST);
        STATES.add(State.FIRST);
        QUEUE.push(node);
        System.out.println("Создана начальная вершина:");
        System.out.println(node.state);
    }

    /**
     * Поиск пути решения и запись в поток
     *
     * @param writer поток для записи пути
     * @throws IOException
     */
    @Override
    void backtracking(Writer writer) throws IOException {
        LinkedList<Node> list = new LinkedList<>();
        list.add(this);
        while (list.getFirst().parent != null) {
            list.addFirst(list.getFirst().parent);
        }
        writer.write("Решиение путём поиска в глубину:\r\n");
        for (Node node : list) {
            writer.write(node.action);
        }
        writer.close();
    }

    /**
     * Проверяет вершину на достижение конечного состояния
     * и затем раскрывает её
     *
     * @return {@code true} если мы достигли конца (нашли решение или закончили, не найдя его)
     */
    @Override
    boolean expand() {
        System.out.println("Текущая вершина:");
        System.out.println(state);
        System.out.println("Глубина: " + depth + "; Стоимость: " + cost + "; Действие: " + action + ";");
        if (state.isTarget()) {
            System.out.println("Поиск закончен");
            System.out.println("Количество созданных вершин: " + countNodes);
            System.out.println("Количество пройденных шагов:  " + countSteps);
            try {
                backtracking(new FileWriter("solution.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            State tmp;

            if (state.canMoveLeft()) {
                tmp = state.moveLeft();
                if (STATES.add(tmp)) {
                    QUEUE.push(new NodeDepth(depth + 1, this, cost + 1, 'L', tmp));
                    System.out.println("Создана вершина действием L:");
                    System.out.println(tmp);
                }
            }

            if (state.canMoveDown()) {
                tmp = state.moveDown();
                if (STATES.add(tmp)) {
                    QUEUE.push(new NodeDepth(depth + 1, this, cost + 1, 'D', tmp));
                    System.out.println("Создана вершина действием D:");
                    System.out.println(tmp);
                }
            }
            if (state.canMoveRight()) {
                tmp = state.moveRight();
                if (STATES.add(tmp)) {
                    QUEUE.push(new NodeDepth(depth + 1, this, cost + 1, 'R', tmp));
                    System.out.println("Создана вершина действием R:");
                    System.out.println(tmp);
                }
            }
            if (state.canMoveUp()) {
                tmp = state.moveUp();
                if (STATES.add(tmp)) {
                    QUEUE.push(new NodeDepth(depth + 1, this, cost + 1, 'U', tmp));
                    System.out.println("Создана вершина действием U:");
                    System.out.println(tmp);
                }
            }
            System.out.println("Всего в очереди вершин, ожидающих раскрытия: " + QUEUE.size());
            System.out.println("-----------------------------------------------");
            return false;
        }
    }

    /**
     * Делаем следующий шаг
     *
     * @return {@code true} если ещё есть шаги для поиска решения
     */
    static boolean nextStep() {
        if (!QUEUE.isEmpty()) {
            countSteps++;
            return !QUEUE.pop().expand();
        } else {
            System.out.println("Решение не найдено.");
            return false;
        }
    }
}