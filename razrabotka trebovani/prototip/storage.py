users = {}
sent_vacancies = set()


def save_filters(user_id: int, text: str, salary: int):
    users[user_id] = {
        "text": text,
        "salary": salary
    }


def get_filters(user_id: int):
    return users.get(user_id)


def is_sent(vacancy_id: str) -> bool:
    return vacancy_id in sent_vacancies


def mark_sent(vacancy_id: str):
    sent_vacancies.add(vacancy_id)
