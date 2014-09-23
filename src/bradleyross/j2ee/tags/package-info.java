/**
 * Demonstration tags for use with Java Server Pages.
 * 
 * <p>Classes used for creating tags include the following:</p>
 * <ul>
 * <li>{@link javax.servlet.jsp.tagext.BodyTagSupport} (JSP tag)</li>
 * <li>{@link javax.servlet.jsp.tagext.ConditionalTagSupport} 
 *       (JSTL library)</li>
 * <li>{@link javax.servlet.jsp.jstl.core.LoopTagSupport} (JSTL library)</li>
 * <li>{@link javax.servlet.jsp.tagxt.SimpleTagSupport} (JSP tag)</li>
 * </ul>
 * <p>The following base classes for tags are used in Java Server Faces</p>
 * <ul>
 * <li>{@link javax.faces.webapp.AttributeTag}</li>
 * <li>{@link javax.faces.webapp.ConverterELTag}</li>
 * <li>{@link javax.faces.webapp.FacetTag}</li>
 * <li>{@link javax.faces.webapp.ValidatorELTag}</li>
 * </ul>
 * 
 * <p>The following is the cycle for processing JSP and JSTL tags 
 *    (see {@link javax.servlet.jsp.tagext.Tag},
 *    {@link javax.servlet.jsp.tagext.BodyTag}), 
 *    {@link javax.servlet.jsp.tagext.IterationTag}.</p>
 * <ul>
 * <li>doStartTag (Can return SKIP_BODY, EVAL_BODY_BUFFERED, or EVAL_BODY_INCLUDE)</li>
 * <li>doInitBody (no return value)</li>
 * <li>doAfterBody (Can return SKIP_BODY or EVAL_BODY_AGAIN)</li>
 * <li>doEndTag (Can return SKIP_PAGE or EVAL_PAGE)</li>
 * </ul>
 */
package bradleyross.j2ee.tags;