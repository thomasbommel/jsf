


function performMainCameraFlight() {
  setCameraToDefaultValues();

  animateSun = true;
  /*
  let y = new CameraAnimation([
    {position: [226,22,437], rotation: {x: 28.5, y:-5.25}, duration: 2},
    {position: [403,137,149], rotation: {x: 243, y:-13}, duration: 5}
  ]);
  */

  let y = new CameraAnimation([
    {position: [356.25, 22.06, 264.20], rotation: {x: -128.28, y:-10.24}, duration: 1},
    {position: [356.25, 22.06, 264.20], rotation: {x: -128.28, y:-10.24}, duration: 1},
    {position: [307.05, 19.63, 230.14], rotation: {x: -160, y:-4}, duration: 2},
    {position: [335.49, 93.99, 55.83], rotation: {x:-104.25,y:-25.75}, duration: 6}, // farm house

    {position: [219.46, 45.61, -100.98], rotation: {x: -40, y:2}, duration: 2},
  //  {position: [-52.36, 76.67, -103.21], rotation: {x: -10, y:2}, duration: 1},
    {position: [-200.36, 100.67, -103.21], rotation: {x: 2, y:2}, duration: 2},
    {position: [-301.27, 121.91, -53.17], rotation: {x: 37, y:-22}, duration: 2},// castle
    {position: [-61.57, 200.80, 109.66], rotation: {x: -64, y:-61}, duration: 6},// c. far awy
    {position: [-146.90, 16.42, 15.06], rotation: {x: -10, y:1}, duration: 3},// c. nearby

    {position: [-38.63, 97.16, 167.50], rotation: {	x:+5,y:2.5}, duration: 3},// middle of map
    //{position: [299.41, 43.42, 305.65], rotation: {	x:-35.28,y:1.62}, duration: 4},// dock
  ]);

  y.startAnimation();
}
