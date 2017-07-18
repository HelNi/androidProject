<?php
/**
 * User: nsh
 * Date: 10.07.2017
 * Time: 12:09
 */

namespace AppBundle\Form;


use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AppDateType extends AbstractType
{
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'input' => 'datetime',
            'widget' => 'single_text'
        ]);
    }

    public function getParent()
    {
        return DateTimeType::class;
    }

}