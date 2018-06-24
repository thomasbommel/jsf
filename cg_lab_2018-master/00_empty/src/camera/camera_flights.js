


function performMainCameraFlight() {
  setCameraToDefaultValues();

  let y = new CameraAnimation([
    {position: [226,22,437], rotation: {x: 28.5, y:-5.25}, duration: 2},
    {position: [403,137,149], rotation: {x: 243, y:-13}, duration: 5}
  ]);
  y.startAnimation();
}
