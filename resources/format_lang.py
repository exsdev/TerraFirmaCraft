from typing import Tuple

import json


def main(validate: bool, langs: Tuple[str, ...]):
    en_us = load('en_us')
    for lang in langs:
        if lang != 'en_us':
            format_lang(en_us, lang, validate)

def update(langs: Tuple[str, ...]):
    en_us = load('en_us')
    en_us_old = load_old('en_us')
    updated_keys = {k for k in en_us.keys() if k in en_us_old and en_us[k] != en_us_old[k]}

    if updated_keys:
        print('Found %d modified values:' % len(updated_keys))
        for k in updated_keys:
            print('Modified: %s : "%s" -> "%s"' % (k, en_us_old[k], en_us[k]))

        inp = input('Remove these keys from other translations?\n(yes|no) >')
        print('Answer: %s' % inp)
        if inp == 'yes':
            # Strip these keys from en_us, so they don't show up in translations
            for k in updated_keys:
                del en_us[k]
            for lang in langs:
                if lang != 'en_us':
                    format_lang(en_us, lang, False)
    else:
        print('No differences found')


def format_lang(en_us, lang: str, validate: bool):
    lang_data = load(lang)

    formatted_lang_data = {}
    extra = 0
    for k, v in lang_data.items():
        if '__comment' in k:
            formatted_lang_data[k] = v
            extra += 1

    for k, _ in en_us.items():
        if k in lang_data:
            formatted_lang_data[k] = lang_data[k]

    print('Translation progress for %s: %d / %d (%.1f%%)' % (lang, (len(lang_data) - extra), len(en_us), 100 * (len(lang_data) - extra) / len(en_us)))
    save(lang, formatted_lang_data, validate)


def load(lang: str):
    with open('./src/main/resources/assets/tfc/lang/%s.json' % lang, 'r', encoding='utf-8') as f:
        return json.load(f)


def load_old(lang: str):
    with open('./%s.old.json' % lang, 'r', encoding='utf-8') as f:
        return json.load(f)


def save(lang: str, lang_data, validate: bool):
    if validate:
        with open('./src/main/resources/assets/tfc/lang/%s.json' % lang, 'r', encoding='utf-8') as f:
            old_lang_data = json.load(f)
            assert old_lang_data == lang_data, 'Validation error in mod localization for %s' % lang
    else:
        with open('./src/main/resources/assets/tfc/lang/%s.json' % lang, 'w', encoding='utf-8') as f:
            json.dump(lang_data, f, ensure_ascii=False, indent=2)
