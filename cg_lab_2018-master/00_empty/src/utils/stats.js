
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
  updateStat("camera.rotation", "x:"+camera.rotation.x.toFixed(2)+",y:"+camera.rotation.y.toFixed(2));
  updateStat("camera.target", vectorToString(camera.target));
  updateStat("movespeed", movementSpeed);
}



function updatePannelFromCamera(){
  document.getElementById('updatePannelFromCamera').innerHTML = "updating";
  let positionx = document.getElementById('positionx');
  let positiony = document.getElementById('positiony');
  let positionz = document.getElementById('positionz');

  positionx.value = camera.position[0];
  positiony.value = camera.position[1];
  positionz.value = camera.position[2];

  let targetx = document.getElementById('targetx');
  let targety = document.getElementById('targety');
  let targetz = document.getElementById('targetz');

  targetx.value = camera.target[0];
  targety.value = camera.target[1];
  targetz.value = camera.target[2];

  let rotx = document.getElementById('rotationx');
  let roty = document.getElementById('rotationy');

  rotx.value = camera.rotation.x;
  roty.value = camera.rotation.y;

  setTimeout(() => {
    document.getElementById('updatePannelFromCamera').innerHTML = "updatePannelFromCamera"
  }, 200)
}


function updateCameraFromPannel(){
  document.getElementById('updateCameraFromPannel').innerHTML = "updating";
  let positionx = parseFloat(document.getElementById('positionx').value);
  let positiony = parseFloat(document.getElementById('positiony').value);
  let positionz = parseFloat(document.getElementById('positionz').value);
  let pos = [positionx,positiony,positionz];
  camera.position = pos;

  let targetx = parseFloat(document.getElementById('targetx').value);
  let targety = parseFloat(document.getElementById('targety').value);
  let targetz = parseFloat(document.getElementById('targetz').value);
  let tar = [targetx,targety,targetz];
  camera.target = tar;

  let rotx = parseFloat(document.getElementById('rotationx').value);
  let roty = parseFloat(document.getElementById('rotationy').value);
  camera.rotation.x = rotx;
  camera.rotation.y = roty;

  setTimeout(() => {
    document.getElementById('updateCameraFromPannel').innerHTML = "updateCameraFromPannel"
  }, 200)
  updateStats();
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
