package io.kimmking.rpcfx.utils;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/13 23:44
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caojun on 2018/2/20.
 *
 * 基本概念：
 * weight: 配置文件中指定的该后端的权重，这个值是固定不变的。
 * effective_weight: 后端的有效权重，初始值为weight。
 * 在释放后端时，如果发现和后端的通信过程中发生了错误，就减小effective_weight。
 * 此后有新的请求过来时，在选取后端的过程中，再逐步增加effective_weight，最终又恢复到weight。
 * 之所以增加这个字段，是为了当后端发生错误时，降低其权重。
 * current_weight:
 * 后端目前的权重，一开始为0，之后会动态调整。那么是怎么个动态调整呢？
 * 每次选取后端时，会遍历集群中所有后端，对于每个后端，让它的current_weight增加它的effective_weight，
 * 同时累加所有后端的effective_weight，保存为total。
 * 如果该后端的current_weight是最大的，就选定这个后端，然后把它的current_weight减去total。
 * 如果该后端没有被选定，那么current_weight不用减小。
 *
 * 算法逻辑：
 * 1. 对于每个请求，遍历集群中的所有可用后端，对于每个后端peer执行：
 *     peer->current_weight += peer->effecitve_weight。
 *     同时累加所有peer的effective_weight，保存为total。
 * 2. 从集群中选出current_weight最大的peer，作为本次选定的后端。
 * 3. 对于本次选定的后端，执行：peer->current_weight -= total。
 *
 */
public class RoundRobinByWeightLoadBalance {

    //约定的invoker和权重的键值对
    final private List<Node> nodes;

    public RoundRobinByWeightLoadBalance(Map<Invoker, Integer> invokersWeight){
        if (invokersWeight != null && !invokersWeight.isEmpty()) {
            nodes = new ArrayList<>(invokersWeight.size());
            invokersWeight.forEach((invoker, weight)->nodes.add(new Node(invoker, weight)));
        }else
            nodes = null;
    }

    /**
     * 算法逻辑：
     * 1. 对于每个请求，遍历集群中的所有可用后端，对于每个后端peer执行：
     *     peer->current_weight += peer->effecitve_weight。
     *     同时累加所有peer的effective_weight，保存为total。
     * 2. 从集群中选出current_weight最大的peer，作为本次选定的后端。
     * 3. 对于本次选定的后端，执行：peer->current_weight -= total。
     *
     * @Return ivoker
     */
    public Invoker select(){
        if (! checkNodes())
            return null;
        else if (nodes.size() == 1) {
            if (nodes.get(0).invoker.isAvalable())
                return nodes.get(0).invoker;
            else
                return null;
        }
        Integer total = 0;
        Node nodeOfMaxWeight = null;
        for (Node node : nodes) {
            total += node.effectiveWeight;
            node.currentWeight += node.effectiveWeight;

            if (nodeOfMaxWeight == null) {
                nodeOfMaxWeight = node;
            }else{
                nodeOfMaxWeight = nodeOfMaxWeight.compareTo(node) > 0 ? nodeOfMaxWeight : node;
            }
        }

        nodeOfMaxWeight.currentWeight -= total;
        return nodeOfMaxWeight.invoker;
    }

    public void onInvokeSuccess(Invoker invoker){
        if (checkNodes()){
            nodes.stream()
                    .filter((Node node)->invoker.id().equals(node.invoker.id()))
                    .findFirst()
                    .get()
                    .onInvokeSuccess();
        }
    }

    public void onInvokeFail(Invoker invoker){
        if (checkNodes()){
            nodes.stream()
                    .filter((Node node)->invoker.id().equals(node.invoker.id()))
                    .findFirst()
                    .get()
                    .onInvokeFail();
        }
    }

    private boolean checkNodes(){
        return (nodes != null && nodes.size() > 0);
    }

    public void printCurrenctWeightBeforeSelect(){
        if (checkNodes()) {
            final StringBuffer out = new StringBuffer("{");
            nodes.forEach(node->out.append(node.invoker.id())
                    .append("=")
                    .append(node.currentWeight+node.effectiveWeight)
                    .append(","));
            out.append("}");
            System.out.print(out);
        }
    }

    public void printCurrenctWeight(){
        if (checkNodes()) {
            final StringBuffer out = new StringBuffer("{");
            nodes.forEach(node->out.append(node.invoker.id())
                    .append("=")
                    .append(node.currentWeight)
                    .append(","));
            out.append("}");
            System.out.print(out);
        }
    }

    public interface Invoker{
        Boolean isAvalable();
        String id();
    }

    private static class Node implements Comparable<Node>{
        final Invoker invoker;
        final Integer weight;
        Integer effectiveWeight;
        Integer currentWeight;

        Node(Invoker invoker, Integer weight){
            this.invoker = invoker;
            this.weight = weight;
            this.effectiveWeight = weight;
            this.currentWeight = 0;
        }

        @Override
        public int compareTo(Node o) {
            return currentWeight > o.currentWeight ? 1 : (currentWeight.equals(o.currentWeight) ? 0 : -1);
        }

        public void onInvokeSuccess(){
            if (effectiveWeight < this.weight)
                effectiveWeight++;
        }

        public void onInvokeFail(){
            effectiveWeight--;
        }
    }

    public static void main(String[] args){
        Map<Invoker, Integer> invokersWeight = new HashMap<>(3);
        Integer aWeight = 4;
        Integer bWeight = 2;
        Integer cWeight = 1;

        invokersWeight.put(new Invoker() {
            @Override
            public Boolean isAvalable() {
                return true;
            }
            @Override
            public String id() {
                return "a";
            }
        }, aWeight);

        invokersWeight.put(new Invoker() {
            @Override
            public Boolean isAvalable() {
                return true;
            }
            @Override
            public String id() {
                return "b";
            }
        }, bWeight);

        invokersWeight.put(new Invoker() {
            @Override
            public Boolean isAvalable() {
                return true;
            }
            @Override
            public String id() {
                return "c";
            }
        }, cWeight);

        Integer times = 7;
        RoundRobinByWeightLoadBalance roundRobin = new RoundRobinByWeightLoadBalance(invokersWeight);
        for(int i=1; i<=times; i++){
            System.out.print(new StringBuffer(i+"").append("    "));
            roundRobin.printCurrenctWeightBeforeSelect();
            Invoker invoker = roundRobin.select();
            System.out.print(new StringBuffer("    ").append(invoker.id()).append("    "));
            roundRobin.printCurrenctWeight();
            System.out.println();
        }
    }
}
