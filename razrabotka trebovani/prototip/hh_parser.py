import requests
from typing import List, Dict
from config import HH_API_URL


def get_vacancies(text: str, salary: int) -> List[Dict]:
    params = {
        "text": text,
        "salary": salary,
        "currency": "RUR",
        "only_with_salary": True,
        "per_page": 5
    }

    response = requests.get(HH_API_URL, params=params, timeout=10)
    response.raise_for_status()

    data = response.json()
    vacancies = []

    for item in data.get("items", []):
        vacancies.append({
            "id": item["id"],
            "title": item["name"],
            "company": item["employer"]["name"],
            "salary": item["salary"]["from"],
            "url": item["alternate_url"]
        })

    return vacancies
