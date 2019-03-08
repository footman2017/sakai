var fluid_1_4=fluid_1_4||{};(function($,fluid){var resourceCache={};var pendingClass={};fluid.fetchResources=function(resourceSpecs,callback,options){var that=fluid.initLittleComponent("fluid.fetchResources",options);that.resourceSpecs=resourceSpecs;that.callback=callback;that.operate=function(){fluid.fetchResources.fetchResourcesImpl(that)};fluid.each(resourceSpecs,function(resourceSpec){resourceSpec.recurseFirer=fluid.event.getEventFirer();resourceSpec.recurseFirer.addListener(that.operate);if(resourceSpec.url&&!resourceSpec.href){resourceSpec.href=resourceSpec.url}});if(that.options.amalgamateClasses){fluid.fetchResources.amalgamateClasses(resourceSpecs,that.options.amalgamateClasses,that.operate)}that.operate();return that};fluid.fetchResources.amalgamateClasses=function(specs,classes,operator){fluid.each(classes,function(clazz){var pending=pendingClass[clazz];fluid.each(pending,function(pendingrec,canon){specs[clazz+"!"+canon]=pendingrec;pendingrec.recurseFirer.addListener(operator)})})};fluid.fetchResources.timeSuccessCallback=function(resourceSpec){if(resourceSpec.timeSuccess&&resourceSpec.options&&resourceSpec.options.success){var success=resourceSpec.options.success;resourceSpec.options.success=function(){var startTime=new Date();var ret=success.apply(null,arguments);fluid.log("External callback for URL "+resourceSpec.href+" completed - callback time: "+(new Date().getTime()-startTime.getTime())+"ms");return ret}}};function canonUrl(url){return url}fluid.fetchResources.clearResourceCache=function(url){if(url){delete resourceCache[canonUrl(url)]}else{fluid.clear(resourceCache)}};fluid.fetchResources.handleCachedRequest=function(resourceSpec,response){var canon=canonUrl(resourceSpec.href);var cached=resourceCache[canon];if(cached.$$firer$$){fluid.log("Handling request for "+canon+" from cache");var fetchClass=resourceSpec.fetchClass;if(fetchClass&&pendingClass[fetchClass]){fluid.log("Clearing pendingClass entry for class "+fetchClass);delete pendingClass[fetchClass][canon]}resourceCache[canon]=response;cached.fire(response)}};fluid.fetchResources.completeRequest=function(thisSpec,recurseCall){thisSpec.queued=false;thisSpec.completeTime=new Date();fluid.log("Request to URL "+thisSpec.href+" completed - total elapsed time: "+(thisSpec.completeTime.getTime()-thisSpec.initTime.getTime())+"ms");thisSpec.recurseFirer.fire()};fluid.fetchResources.makeResourceCallback=function(thisSpec){return{success:function(response){thisSpec.resourceText=response;thisSpec.resourceKey=thisSpec.href;if(thisSpec.forceCache){fluid.fetchResources.handleCachedRequest(thisSpec,response)}fluid.fetchResources.completeRequest(thisSpec)},error:function(response,textStatus,errorThrown){thisSpec.fetchError={status:response.status,textStatus:response.textStatus,errorThrown:errorThrown};fluid.fetchResources.completeRequest(thisSpec)}}};fluid.fetchResources.issueCachedRequest=function(resourceSpec,options){var canon=canonUrl(resourceSpec.href);var cached=resourceCache[canon];if(!cached){fluid.log("First request for cached resource with url "+canon);cached=fluid.event.getEventFirer();cached.$$firer$$=true;resourceCache[canon]=cached;var fetchClass=resourceSpec.fetchClass;if(fetchClass){if(!pendingClass[fetchClass]){pendingClass[fetchClass]={}}pendingClass[fetchClass][canon]=resourceSpec}options.cache=false;$.ajax(options)}else{if(!cached.$$firer$$){options.success(cached)}else{fluid.log("Request for cached resource which is in flight: url "+canon);cached.addListener(function(response){options.success(response)})}}};fluid.fetchResources.composeCallbacks=function(internal,external){return external?function(){try{external.apply(null,arguments)}catch(e){fluid.log("Exception applying external fetchResources callback: "+e)}internal.apply(null,arguments)}:internal};fluid.fetchResources.composePolicy=function(target,source,key){return fluid.fetchResources.composeCallbacks(target,source)};fluid.defaults("fluid.fetchResources.issueRequest",{mergePolicy:{success:fluid.fetchResources.composePolicy,error:fluid.fetchResources.composePolicy,url:"reverse"}});fluid.fetchResources.issueRequest=function(resourceSpec,key){var thisCallback=fluid.fetchResources.makeResourceCallback(resourceSpec);var options={url:resourceSpec.href,success:thisCallback.success,error:thisCallback.error,dataType:"text"};fluid.fetchResources.timeSuccessCallback(resourceSpec);fluid.merge(fluid.defaults("fluid.fetchResources.issueRequest").mergePolicy,options,resourceSpec.options);resourceSpec.queued=true;resourceSpec.initTime=new Date();fluid.log("Request with key "+key+" queued for "+resourceSpec.href);if(resourceSpec.forceCache){fluid.fetchResources.issueCachedRequest(resourceSpec,options)}else{$.ajax(options)}};fluid.fetchResources.fetchResourcesImpl=function(that){var complete=true;var allSync=true;var resourceSpecs=that.resourceSpecs;for(var key in resourceSpecs){var resourceSpec=resourceSpecs[key];if(!resourceSpec.options||resourceSpec.options.async){allSync=false}if(resourceSpec.href&&!resourceSpec.completeTime){if(!resourceSpec.queued){fluid.fetchResources.issueRequest(resourceSpec,key)}if(resourceSpec.queued){complete=false}}else{if(resourceSpec.nodeId&&!resourceSpec.resourceText){var node=document.getElementById(resourceSpec.nodeId);resourceSpec.resourceText=fluid.dom.getElementText(node);resourceSpec.resourceKey=resourceSpec.nodeId}}}if(complete&&that.callback&&!that.callbackCalled){that.callbackCalled=true;if($.browser.mozilla&&!allSync){setTimeout(function(){that.callback(resourceSpecs)},1)}else{that.callback(resourceSpecs)}}};fluid.fetchResources.primeCacheFromResources=function(componentName){var resources=fluid.defaults(componentName).resources;var that={typeName:"fluid.fetchResources.primeCacheFromResources"};var expanded=(fluid.expandOptions?fluid.expandOptions:fluid.identity)(fluid.copy(resources),that);fluid.fetchResources(expanded)};fluid.registerNamespace("fluid.expander");fluid.expander.makeDefaultFetchOptions=function(successdisposer,failid,options){return $.extend(true,{dataType:"text"},options,{success:function(response,environmentdisposer){var json=JSON.parse(response);environmentdisposer(successdisposer(json))},error:function(response,textStatus){fluid.log("Error fetching "+failid+": "+textStatus)}})};fluid.expander.makeFetchExpander=function(options){return{expander:{type:"fluid.expander.deferredFetcher",href:options.url,options:fluid.expander.makeDefaultFetchOptions(options.disposer,options.url,options.options),resourceSpecCollector:"{resourceSpecCollector}",fetchKey:options.fetchKey}}};fluid.expander.deferredFetcher=function(target,source,recurse,expandOptions){var expander=source.expander;var spec=fluid.copy(expander);var collector=fluid.resolveEnvironment(expander.resourceSpecCollector,expandOptions);delete spec.type;delete spec.resourceSpecCollector;delete spec.fetchKey;var environmentdisposer=function(disposed){$.extend(target,disposed)};spec.options.success=function(response){expander.options.success(response,environmentdisposer)};var key=expander.fetchKey||fluid.allocateGuid();collector[key]=spec;return target}})(jQuery,fluid_1_4);