/**
 * 
 */
package jp.neap.hanne;

/**
 *
 */
public class SpinnerHandleNameBean extends HandleNameBean {

	private final String spinnetLabel;
	
	public SpinnerHandleNameBean(
			int id,
			int appType,
			String loginName,
			String password,
			String handleName,
			int request,
			String spinnetLabel
			) {
		super(id, appType, loginName, password, handleName, request);
		this.spinnetLabel = spinnetLabel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return spinnetLabel;
	}
}
