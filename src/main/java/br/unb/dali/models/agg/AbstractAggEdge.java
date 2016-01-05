package br.unb.dali.models.agg;

import agg.attribute.AttrInstance;
import agg.attribute.impl.AttrTupleManager;
import agg.xt_basis.Arc;
import agg.xt_basis.Node;
import agg.xt_basis.Type;
import br.unb.dali.models.agg.exceptions.InconsistentEdgeTypeException;
import br.unb.dali.models.agg.exceptions.NullArcException;
import br.unb.dali.models.agg.exceptions.NullSourceOfAggEdgeException;
import br.unb.dali.models.agg.exceptions.NullTargetOfAggEdgeException;

/**
 * This abstract class defines the expected behavior of any class identified as an Agg Edge;
 * 
 * 
 * Property {@link #_aggArc} defines the underlying agg arc of this uml activity diagram edge
 * Property {@link #_type} defines the underlying agg type of this uml activity diagram edge
 * Property {@link #_source} defines the node where this activity edge is generated from  
 * Property {@link #_target} defines the node where this activity is pointing to
 * Property {@link #_context} defines the hidden context at which the instance of this class is at
 * 
 * @author abiliooliveira
 */
public abstract class AbstractAggEdge {
	protected AbstractAggModel _context;
	protected Type _type;
	protected Arc _aggArc;
	protected AbstractAggNode _source;
	protected AbstractAggNode _target;
	
	/**
	 * @return the underlying agg arc
	 */
	public Arc getAggArc() {
		return _aggArc;
	}
	
	/**
	 * @return the underlying agg type of this edge
	 */
	public Type getAggType() {
		return _type;
	}
	
	/**
	 * alias to source.getAggNode()
	 * @return the underlying agg node of the UML AD source node
	 */
	public Node getAggSourceNode() {
		return _source.getAggNode();
	}
	
	/**
	 * alias to target.getAggNode()
	 * @return the underlying agg node of the UML AD target node
	 */
	public Node getAggTargetNode() {
		return _target.getAggNode();
	}
	
	/**
	 * This model sets up the edge structures based on the properties of _aggArc;
	 * 
	 * Every AnAggEdge subclass MUST implement this method, since it is always called by the constructors
	 */
	protected abstract void setUp();
	
	/**
	 * Constructs a new agg edge, forcing the call to setType
	 * $source and $target MUST NOT be null
	 * 
	 * @param source must not be null
	 * @param target must not be null
	 * @throws NullSourceOfAggEdgeException 
	 * @throws NullTargetOfAggEdgeException 
	 */
	public AbstractAggEdge(AbstractAggNode source, AbstractAggNode target, AbstractAggModel context) throws NullSourceOfAggEdgeException, NullTargetOfAggEdgeException {
		if (source == null) throw new NullSourceOfAggEdgeException();
		if (target == null) throw new NullTargetOfAggEdgeException();
		setUnderlyingInfo(source, target, context);
		setUp();
	}

	/**
	 * Constructs a new agg edge based on an agg arc;
	 * 
	 * @param arc the agg arc from where the information will be gathered;  MUST NOT be null 
	 * @throws NullArcException  
	 * @throws NullTargetOfAggEdgeException 
	 * @throws NullSourceOfAggEdgeException 
	 * @throws InconsistentEdgeTypeException 
	 */
	public AbstractAggEdge(Arc arc, AbstractAggModel context) throws NullArcException, NullSourceOfAggEdgeException, NullTargetOfAggEdgeException, InconsistentEdgeTypeException {
		if (arc == null) throw new NullArcException();
		setUnderlyingInfo(arc, context);
		setUp();
	}
	
	/**
	 * Sets the hidden info from an AbstractAggNode source and an AbstractAggNode target;
	 *   
	 * @param source
	 * @param target
	 * @param context
	 */
	private void setUnderlyingInfo(AbstractAggNode source, AbstractAggNode target, AbstractAggModel context) {
		setCommonUnderlyingInfo(source, target, context);
		AttrInstance tt = AttrTupleManager.getDefaultManager().newInstance(
				_type.getAttrType(), null);
		_aggArc = new Arc(tt,_type, _source.getAggNode(), _target.getAggNode(), _context.getGraph());
	}

	/**
	 * Sets the hidden info from an agg arc;
	 * 
	 * @param arc
	 * @param context
	 * @throws NullSourceOfAggEdgeException 
	 * @throws NullTargetOfAggEdgeException 
	 * @throws InconsistentEdgeTypeException 
	 */
	private void setUnderlyingInfo(Arc arc, AbstractAggModel context) throws NullSourceOfAggEdgeException, NullTargetOfAggEdgeException, InconsistentEdgeTypeException {
		if (!arc.getType().getName().equals(this.getClass().getSimpleName()))
			throw new InconsistentEdgeTypeException();
		
		AbstractAggNode source = context.searchNode((Node)arc.getSource());
		if (source == null) throw new NullSourceOfAggEdgeException();
		AbstractAggNode target = context.searchNode((Node)arc.getTarget());
		if (target == null) throw new NullTargetOfAggEdgeException();
		
		setCommonUnderlyingInfo(source, target, context);
		_aggArc = arc;
	}
	
	/**
	 * To avoid code redundancy, the setting of the properties _context, _type, _source and _target
	 * were encapsulated in this method;
	 * 
	 * @param source
	 * @param target
	 * @param context
	 */
	private void setCommonUnderlyingInfo(AbstractAggNode source, AbstractAggNode target, AbstractAggModel context) {
		 _context = context;
		_type = context.getGraGra().getTypeSet().getTypeByName(this.getClass().getSimpleName());
		_source = source;
		_target = target;
		
	}
	
}
