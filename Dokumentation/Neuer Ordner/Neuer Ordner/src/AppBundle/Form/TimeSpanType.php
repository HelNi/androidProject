<?php
/**
 * User: nsh
 * Date: 11.07.2017
 * Time: 08:12
 */

namespace AppBundle\Form;


use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Callback;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\NotNull;
use Symfony\Component\Validator\Context\ExecutionContextInterface;

class TimeSpanType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
        ->add('start', AppDateType::class, ['constraints' => [new NotBlank()]])
        ->add('end', AppDateType::class, ['constraints' => [new NotBlank()]]);
    }

    public static function validateDates($data, ExecutionContextInterface $context)
    {
        if ($data['start'] > $data['end']) {
            $context
                ->buildViolation('Das Ende muss nach dem Start liegen')
                ->atPath('end')
                ->addViolation();
        }
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefault('constraints', [new Callback(['callback' => [TimeSpanType::class, 'validateDates']])]);
        $resolver->setDefault('allow_extra_fields', \true);
    }


}