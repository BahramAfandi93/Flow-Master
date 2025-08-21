/**
 * Show/hide shape-specific fields when the shape changes.
 */
function toggleShapeFields() {
  const shape = document.getElementById("shape").value;
  const circleFields = document.querySelectorAll(".circle-fields");
  const boxFields = document.querySelectorAll(".box-fields");
  if (shape === "CIRCLE_CULVERT") {
    circleFields.forEach((el) => (el.style.display = "flex"));
    boxFields.forEach((el) => (el.style.display = "none"));
  } else {
    circleFields.forEach((el) => (el.style.display = "none"));
    boxFields.forEach((el) => (el.style.display = "flex"));
  }
}

/**
 * Collects form data, sends a POST request to the backend, and renders the response.
 */
async function calculate() {
  // Build the request payload
  const chainage = document.getElementById("chainage").value;
  const material = document.getElementById("material").value;
  const flowHeight = parseFloat(document.getElementById("flowHeight").value);
  const rainIntensity = parseFloat(
    document.getElementById("rainIntensity").value
  );
  const calculationArea = parseFloat(
    document.getElementById("calculationArea").value
  );
  const slope = parseFloat(document.getElementById("slope").value);
  const shape = document.getElementById("shape").value;
  const diameter = parseFloat(
    document.getElementById("structureDiameter").value
  );
  const width = parseFloat(document.getElementById("structureWidth").value);
  const height = parseFloat(document.getElementById("structureHeight").value);

  const requestBody = {
    chainage,
    material,
    flowHeight: isNaN(flowHeight) ? null : flowHeight,
    rainIntensity: isNaN(rainIntensity) ? null : rainIntensity,
    calculationArea: isNaN(calculationArea) ? null : calculationArea,
    slope: isNaN(slope) ? null : slope,
    shape,
    structureDiameter:
      shape === "CIRCLE_CULVERT" ? (isNaN(diameter) ? null : diameter) : null,
    structureWidth:
      shape === "BOX_CULVERT" ? (isNaN(width) ? null : width) : null,
    structureHeight:
      shape === "BOX_CULVERT" ? (isNaN(height) ? null : height) : null,
  };

  const resultContainer = document.getElementById("result");
  resultContainer.textContent = "Calculating...";

  try {
    const response = await fetch(
      "http://localhost:8080/simple/calculate/culvert",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      }
    );

    if (!response.ok) {
      throw new Error("Failed to fetch calculation result.");
    }
    const data = await response.json();

    // Build a table of results
    let tableHtml = '<table class="result-table">';
    tableHtml += "<thead><tr><th>Field</th><th>Value</th></tr></thead><tbody>";
    const fieldsToDisplay = {
      centralAngle: "Central Angle",
      waterSpeed: "Water Speed",
      wettedPerimeter: "Wetted Perimeter",
      flowArea: "Flow Area",
      hydraulicRadius: "Hydraulic Radius",
      minAllowedSlope: "Min Allowed Slope",
      roughness: "Roughness",
      flowRate: "Flow Rate",
      requiredFlowRate: "Required Flow Rate",
      result: "Result",
    };
    Object.keys(fieldsToDisplay).forEach((key) => {
      const label = fieldsToDisplay[key];
      const value = data[key] != null ? data[key] : "-";
      tableHtml += `<tr><td>${label}</td><td>${value}</td></tr>`;
    });
    tableHtml += "</tbody></table>";
    resultContainer.innerHTML = tableHtml;
  } catch (error) {
    resultContainer.textContent = error.message;
  }
}

// Initialize shape fields on page load
document.addEventListener("DOMContentLoaded", () => {
  toggleShapeFields();
});
