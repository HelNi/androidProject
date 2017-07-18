<?php

namespace AppBundle\Form;

use AppBundle\Entity\User;
use FOS\UserBundle\Form\Type\RegistrationFormType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('salutation', TextType::class, ['label' => 'user.salutation'])
            ->add('title', null, ['label' => 'user.title', 'required' => \false, 'empty_data' => ''])
            ->add('firstName', null, ['label' => 'user.firstName'])
            ->add('lastName', null, ['label' => 'user.lastName']);
    }

    public function getParent()
    {
        return RegistrationFormType::class;
    }

    public function getBlockPrefix()
    {
        return 'app_bundle_user_type';
    }
}
