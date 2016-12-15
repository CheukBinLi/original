package com.cheuks.bin.original.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Linked<V> implements Cloneable {

	private Node<V> head;
	private Node<V> end;
	private Node<V> current;
	private AtomicInteger size = new AtomicInteger();
	private volatile int index;

	public Linked<V> clone() throws CloneNotSupportedException {
		Linked<V> result = null;
		try {
			result = (Linked<V>) super.clone();
			// if (null != current)
			// result.current = current.clone();
			// if (null != head)
			// result.head = head.clone();
			// if (null != end)
			// result.end = end.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean hasNext() {
		return index < size.get();
	}

	public void reset() {
		index = 0;
		current = head;
	}

	public Node<V> addFirst(Node<V> node) {
		if (null == head) {
			head = node;
			end = node;
		} else {
			head.previous = node;
			node.next = head;
			head = node;
		}
		size.addAndGet(1);
		return head;
	}

	public Linked<V> addFirst(V v) {
		addFirst(new Node<V>(v));
		return this;
	}

	public Node<V> addLast(Node<V> node) {
		if (null == end) {
			head = node;
			end = node;
		} else {
			node.previous = end;
			end.next = node;
			end = node;
		}
		size.addAndGet(1);
		return end;
	}

	public Linked<V> addLast(V v) {
		addLast(new Node<V>(v));
		return this;
	}

	public Node<V> nextNode() {
		if (null == current) {
			current = head;
		}
		Node<V> result = current;
		current = current.next;
		index++;
		return result;
	}

	public V next() {
		if (null == current) {
			current = head;
		}
		V result = current.data;
		current = current.next;
		index++;
		return result;
	}

	public V previous() {
		if (null == current) {
			return null;
		}
		current = current.previous;
		return current.data;
	}

	public final static <T> Node<T> getFirstNode(final Node<T> node) {
		Node<T> tempNode = node;
		Node<T> result = node;
		do {
			tempNode.next = result;
			result = tempNode;
		} while (null != (tempNode = tempNode.getPrevious()));
		return result;
	}

	public final static <T> Node<T> addPreviousNode(final Node<T> head, final Node<T> node) {
		Node<T> tempNode = head.previous;
		if (null != tempNode) {
			tempNode.next = node;
			node.previous = tempNode;
		}
		head.previous = node;
		node.next = head;
		return node;
	}

	public final static <T> Node<T> addNextNode(final Node<T> head, final Node<T> node) {
		Node<T> tempNode = head.next;
		if (null != tempNode) {
			tempNode.previous = node;
			node.next = tempNode;
		}
		head.next = node;
		node.previous = head;
		return node;
	}

	public static class Node<V> implements Serializable, Cloneable {

		private static final long serialVersionUID = 1L;

		private Node<V> previous;
		private Node<V> next;
		private V data;

		@Override
		public Node<V> clone() throws CloneNotSupportedException {
			Node<V> result = null;
			try {
				result = (Node<V>) super.clone();
				// if (null != previous)
				// result.previous = previous.clone();
				// if (null != next)
				// result.next = next.clone();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public Node<V> getPrevious() {
			return previous;
		}

		public Node<V> setPrevious(Node<V> previous) {
			this.previous = previous;
			return this;
		}

		public Node<V> getNext() {
			return next;
		}

		public Node<V> setNext(Node<V> next) {
			this.next = next;
			return this;
		}

		public V getData() {
			return data;
		}

		public Node<V> setData(V data) {
			this.data = data;
			return this;
		}

		public Node(V data) {
			super();
			this.data = data;
		}
	}

	public static void main(String[] args) {
		// Linked<String> linked = new Linked<String>();
		//
		// linked.addLast("A").addLast("B").addLast("C").addLast("D").addLast("E").addLast("F").addLast("G");
		// Node<String> node = linked.addFirst(new Node<String>("X"));
		// linked.addFirst("A").addFirst("B").addFirst("C").addFirst("D").addFirst("E").addFirst("F").addFirst("G");
		//
		// Node<String> X = Linked.getFirstNode(node);
		// Node<String> tempNextNode = X;
		// do {
		// if (node.equals(tempNextNode))
		// break;
		// System.out.print(tempNextNode.getData() + ",");
		// } while (null != (tempNextNode = tempNextNode.getNext()));
		//
		// System.out.println();
		//
		// while (linked.hasNext()) {
		// System.out.print(linked.next() + ",");
		// }
		// System.out.println();
		// linked.reset();
		// while (linked.hasNext()) {
		// System.out.print(linked.next() + ",");
		// }

		Node<String> A = new Node<String>("A");
		Node<String> B = new Node<String>("B");
		Node<String> C = new Node<String>("C");
		Node<String> D = new Node<String>("D");
		Node<String> E = new Node<String>("E");
		Node<String> F = new Node<String>("F");
		Node<String> G = new Node<String>("G");

		B = Linked.addNextNode(A, B);
		C = Linked.addNextNode(B, C);
		D = Linked.addNextNode(C, D);
		E = Linked.addNextNode(D, E);
		F = Linked.addNextNode(E, F);
		G = Linked.addNextNode(F, G);

		Node<String> main = Linked.getFirstNode(G);
		do {
			if (G.equals(main))
				break;
			System.out.print(main.getData() + ",");
		} while (null != (main = main.getNext()));

	}

	public Map<String, Node<String>> v1(List<Map> list, String nodeIdField, String nodeContentField, String subListName, final Node<String> parentNode) {
		Map<String, Node<String>> result = new HashMap<String, Linked.Node<String>>();
		Iterator<Map> it = list.iterator();
		Map next;
		Object subList;
		Node<String> node;
		// Map node;
		while (it.hasNext()) {
			next = it.next();
			// 节点
			// 下一节点
			node = new Node<String>(next.get(nodeContentField).toString());
			if (null != parentNode) {
				node = Linked.addNextNode(parentNode, node);
			}
			if (!result.containsKey(next.get(nodeIdField).toString()))
				result.put(next.get(nodeIdField).toString(), node);
			if (null == (subList = next.get(subListName)))
				continue;
			result.putAll(v1((List<Map>) subList, nodeIdField, nodeContentField, subListName, node));
		}
		return result;
	}

	/***
	 * 链表结构树
	 * 
	 * @param allNode
	 *            所有节点
	 * @param existsNode
	 *            存在的节点
	 * @param idField
	 *            id字段名
	 * @param subListName
	 *            了节点字段名
	 * @return
	 */
	public static Map<String, Map> createNodeByLinked(Map<String, Node<Map>> allNode, List<Map> existsNode, String idField, String subListName) {
		Iterator<Map> it = existsNode.iterator();
		Map next;
		Object subList;
		Node<Map> node;
		Node<Map> head;
		Object id;
		Map<String, Map> result = new HashMap<String, Map>();
		while (it.hasNext()) {
			next = it.next();
			if (null == (id = next.get(idField)))
				continue;
			node = allNode.get(id);
			if (null != node) {
				head = Linked.getFirstNode(node);
				nextNode(head, node, idField, result, subListName);
			}
		}
		return result;
	}

	/***
	 *
	 * @param firstNode
	 *            顶节点
	 * @param endNode
	 *            结束节点
	 * @param idNode
	 *            map（id字段）
	 * @param returnResult
	 *            返回结果
	 * @param subListName
	 *            存放在map中的子节集节点名
	 */
	public static void nextNode(Node<Map> firstNode, Node<Map> endNode, String idNode, final Map<String, Map> returnResult, String subListName) {
		Object id;
		Map<String, Map> node;
		Map<String, Map> subList;
		Node<Map> next;
		if (null == firstNode || null == (id = firstNode.getData().get(idNode)))
			return;
		if (null == (node = returnResult.get(id.toString()))) {
			node = new HashMap();
			node.putAll(firstNode.getData());
			returnResult.put(id.toString(), node);
			subList = new HashMap<String, Map>();
			node.put(subListName, subList);
		} else
			subList = node.get(subListName);
		next = firstNode.getNext();
		if (null == next || firstNode.equals(endNode))
			return;
		nextNode(next, endNode, idNode, subList, subListName);
	}
}
