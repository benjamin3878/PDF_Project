package pdf;

/*
 * This class is defined to hold two paired generic types
 */
public class Pair<V, E> {
	
	private V val;
	private E ele;
	
	public Pair(V val, E ele){
		this.val = val;
		this.ele = ele;
	}
	
	public V getValue(){
		return this.val;
	}
	
	public E getElement(){
		return this.ele;
	}
	
	public void setValue(V val){
		this.val = val;
	}
	
	public void setElement(E ele){
		this.ele = ele;
	}
	
	@Override
	public int hashCode(){
		return this.val.hashCode() ^ this.ele.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(o == this){
			return true;
		}
		if(!(o instanceof Pair<?,?>)){
			return false;
		}
		
		Pair<V,E> other = (Pair<V,E>)o;
		if(this.val.equals(other.val) && this.ele.equals(other.ele)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return "{" + this.val.toString() + ", " + this.ele.toString() + "}";
	}
	
}
