/*
	IE7, version 0.9 (alpha) (2005-08-19)
	Copyright: 2004-2005, Dean Edwards (http://dean.edwards.name/)
	License: http://creativecommons.org/licenses/LGPL/2.1/
*/
IE7.addModule("ie7-dynamic-attributes",function(){if(!modules["ie7-css2-selectors"])return;var attributeSelectors=cssQuery.valueOf("attributeSelectors");var parseSelector=cssQuery.valueOf("parseSelector");function DynamicAttribute(s,a,d,t,c){this.attach=a||"*";parseSelector(d);this.dynamicAttribute=attributeSelectors["@"+d];this.target=t;this.inherit(s,c)};ie7CSS.Rule.specialize({constructor:DynamicAttribute,recalc:function(){var m=cssQuery(this.attach);for(var i=0;i<m.length;i++){var t=(this.target)?cssQuery(this.target,m[i]):[m[i]];if(t.length)this.apply(m[i],t)}},apply:function(e,t){var self=this;addEventHandler(e,"onpropertychange",function(){if(event.propertyName==self.dynamicAttribute.name)self.test(e,t)});this.test(e,t)},test:function(e,t){var a=this.dynamicAttribute.test(e)?"add":"remove";for(var i=0;(e=t[i]);i++)this[a](e)}});DynamicAttribute.MATCH=/(.*)(\[[^\]]*\])(.*)/;StyleSheet.prototype.specialize({createRule:function(s,c){var m;if(m=s.match(DynamicAttribute.MATCH)){return new DynamicAttribute(s,m[1],m[2],m[3],c)}else return this.inherit(s,c)}})});
