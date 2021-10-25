package bufferAndManagers;

import mortgage.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Buffer {
  private final List<Request> buffer;
  private int occupiedCells;

  private final Object bufferNotEmptyNotifier;

  public Buffer(int size, Object bufferNotEmptyNotifier) {
    this.buffer = new ArrayList<>(Collections.nCopies(size, null));
    this.bufferNotEmptyNotifier = bufferNotEmptyNotifier;
  }

  public Request get(int index) {
    Request request = buffer.get(index);
    buffer.set(index, null);
    --occupiedCells;
    return request;
  }

  public void set(int index, Request request) {
    Request oldRequest = buffer.get(index);
    buffer.set(index, request);
    if (oldRequest == null) {
      ++occupiedCells;
      synchronized (bufferNotEmptyNotifier) {
        bufferNotEmptyNotifier.notify();
      }
    }
  }

  public synchronized boolean isEmpty() {
    return occupiedCells == 0;
  }

  public synchronized boolean isFull() {
    return occupiedCells == buffer.size();
  }

  protected List<Request> getRequestsList() {
    return buffer;
  }
}
