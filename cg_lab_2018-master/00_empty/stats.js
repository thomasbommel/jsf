
<!-- TODO remove this script, only for debugging -->


function toggleStats(){
  console.log("toggleStats");
  document.getElementsByTagName('body')[0].classList.toggle('fullscreen');
}


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
  updateStat("camera.position", vectorToString(camera.position));
  updateStat("camera.rotation", vectorToString(camera.rotation));
  updateStat("camera.target", vectorToString(camera.target));
  updateStat("camera.direction", vectorToString(getCameraDirection()));
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
