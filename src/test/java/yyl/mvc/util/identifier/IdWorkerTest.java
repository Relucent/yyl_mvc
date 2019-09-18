package yyl.mvc.util.identifier;

import yyl.mvc.common.identifier.IdWorker;

public class IdWorkerTest {
    /** 测试 */
    public static void main(String[] args) {
        IdWorker idGenerator = new IdWorker(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idGenerator.nextId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }
}
