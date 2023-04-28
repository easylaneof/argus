<#if links?size == 0>
<b>You don't have any links yet.</b>

Add some with /track command
<#else>
<b>Your links:</b>

<#list links as link>
<a href="${link.url()}">${link.url()}</a>
</#list>
</#if>
