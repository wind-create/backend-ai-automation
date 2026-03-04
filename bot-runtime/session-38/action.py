import json
import time
import pyautogui

pyautogui.FAILSAFE = True


def resolve_vars(text, variables):
    if not isinstance(text, str):
        return text

    for k, v in variables.items():
        text = text.replace(f"{{{k}}}", str(v))

    return text


def run_workflow(file):

    with open(file, "r", encoding="utf-8") as f:
        wf = json.load(f)

    vars = wf.get("variables", {}).copy()
    delay = wf["settings"]["default_delay"]

    steps = wf["steps"]

    print("RUN WORKFLOW:", wf["name"])

    i = 0

    while i < len(steps):

        step = steps[i]
        action = step["action"]

        if action == "focus_window":
            print("Focus Excel")

        elif action == "hotkey":
            pyautogui.hotkey(*step["keys"])

        elif action == "press_key":
            pyautogui.press(step["key"])

        elif action == "type_text":
            text = resolve_vars(step["value"], vars)
            pyautogui.typewrite(text)

        elif action == "set_var":
            expr = step["value"][1:-1]
            vars[step["name"]] = eval(expr, {}, vars)

        elif action == "loop":

            times = step["times"]
            from_step = step["from_step"]

            if times > 1:
                step["times"] -= 1

                for idx, s in enumerate(steps):
                    if s["id"] == from_step:
                        i = idx - 1
                        break

        time.sleep(delay)
        i += 1


if __name__ == "__main__":
    run_workflow("workflow_excel_build.json")