package provided.extvisitor.test;

import provided.extvisitor.*;

public abstract class AHostStr implements IHostStr<AHostStr> { 

	private static final long serialVersionUID = -5951881622588273026L;
	
	private String idx = "AHostStr";
	
	public AHostStr(String idx){
		this.idx = idx;
	}
	@Override
	public <R, P> R execute(IExtVisitor<R, String, P, ? extends AHostStr> algo,
			@SuppressWarnings("unchecked") P... params) {
		return algo.caseAt(idx, this, params);
	}

}
