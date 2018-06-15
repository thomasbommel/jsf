
<!-- TODO remove this script, only for debugging -->



var list = [];
/**
 * [updates the new and the old status panel]
 * @param  {[type]} tag  [the tag which will be used (entry with same tag will be overwritten)]
 * @param  {[type]} text [the object or text to display]
 */
function updateStat(tag,text){
  <!-- old status -->
  let oldStatusText = "<table>";
  for(let entry in list){
    oldStatusText += "<tr>"
    oldStatusText += "<td>"+entry + "</td> <td>" + list[entry] + "</td>";
    oldStatusText += "</tr>"
  }
  oldStatusText+"</table>";

  <!-- new status -->
  list[tag]= text;

  let statusText = "<table>";
  for(let entry in list){
    statusText += "<tr>"
    statusText += "<td>"+entry + "</td> <td>" + list[entry] + "</td>";
    statusText += "</tr>"
  }
  statusText+"</table>"
  document.getElementById('oldstats').innerHTML = "<b>OLD</b> <br/>"+oldStatusText;
  document.getElementById('stats').innerHTML = "<b>NEW</b> <br/>"+statusText;
}

/**
 * this function updates all the relevant stats in the old+new stats panel
 */
function updateStats(){
  updateStat("camera.position",vectorToString(camera.position));
  updateStat("camera.rotation",vectorToString(camera.rotation));
  updateStat("camera.target",vectorToString(camera.target));
  updateStat("pos-targ",getDistanceBetweenPoints(camera.position,camera.target));
}

/**
 * this method displays a Vector using the displayText method from the framework
 * @param  {[type]} vec3 [the vector itself]
 * @param  {[type]} name [>>optional<<, prefix for the vector]
 */
function vectorToString(vec3,name){
  var str = "{";
  for(let i in vec3){
    str+= vec3[i].toFixed(2)+", ";
  }
  str+="}";
  str = str.replace(", }","}");
  return str;
}

/** this method returns the distance between two Points */
// XXX TODO FIXME not tested yet (especially think about the 'z' part)
function getDistanceBetweenPoints(aa,bb){
  let a = getXYZVector(aa);
  let b = getXYZVector(bb);

  let deltaX = (typeof a.x === 'undefined' || typeof b.x === 'undefined') ? 0 : a.x - b.x;
  let deltaY = (typeof a.y === 'undefined' || typeof b.y === 'undefined') ? 0 : a.y - b.y;
  let deltaZ = (typeof a.z === 'undefined' || typeof b.z === 'undefined') ? 0 : a.z - b.z;
  distance = Math.sqrt( Math.abs(  deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));

  return distance.toFixed(2);
}

// TODO modify/test/delete this method
function getXYZVector(vec3){
  if( typeof vec3.x  !== "undefined" && typeof vec3.y  !== "undefined"){
    return vec3;
  }
  return{x:vec3[0],
         y:vec3[1],
         z:vec3[2]
  };
}

/**
 * takes a 'snapshot' from the 'new' panel and displays it in the 'snapshot'-panel
 */
function makeSnapshot(){
  document.getElementById('snapshot').innerHTML=document.getElementById('stats').innerHTML;
}
